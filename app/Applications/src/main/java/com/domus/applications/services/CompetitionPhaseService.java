package com.domus.applications.services;

import com.domus.applications.configs.MQConfig;
import com.domus.applications.dto.request.CompetitionPhaseRequestDto;
import com.domus.applications.dto.request.DgesImportRequestDto;
import com.domus.applications.dto.response.ApplicationResponseDto;
import com.domus.applications.entities.Application;
import com.domus.applications.entities.CompetitionPhase;
import com.domus.applications.entities.Competition;
import com.domus.applications.entities.CourseOption;
import com.domus.applications.messages.CreateEnrollmentMessage;
import com.domus.applications.repositories.CompetitionPhaseRepository;
import com.domus.applications.repositories.ApplicationRepository;
import com.domus.applications.repositories.CompetitionRepository;
import com.domus.applications.repositories.CourseOptionRepository;
import com.domus.applications.valueObjects.ApplicantInfo;
import com.domus.applications.valueObjects.ApplicationOrigin;
import com.domus.applications.valueObjects.CourseOptionStatus;
import com.domus.applications.valueObjects.PhaseStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompetitionPhaseService {
    private final ModelMapper mapper;
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationRepository applicationRepository;
    private final CourseOptionRepository courseOptionRepository;
    private final CompetitionPhaseRepository phaseRepository;
    private final CompetitionRepository competitionRepository;

    public CompetitionPhaseService(ModelMapper mapper, RabbitTemplate rabbitTemplate, ApplicationRepository applicationRepository, CourseOptionRepository courseOptionRepository, CompetitionPhaseRepository phaseRepository, CompetitionRepository competitionRepository) {
        this.mapper = mapper;
        this.rabbitTemplate = rabbitTemplate;
        this.applicationRepository = applicationRepository;
        this.courseOptionRepository = courseOptionRepository;
        this.phaseRepository = phaseRepository;
        this.competitionRepository = competitionRepository;
    }

    public CompetitionPhase findById(long id) {
        return phaseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Phase not found with id: " + id));
    }

    public CompetitionPhase createPhase(Long competitionId, CompetitionPhaseRequestDto phaseDto) {
        Competition competition = competitionRepository.findById(competitionId).orElseThrow(
                () -> new EntityNotFoundException("Competition not found with id: " + competitionId)
        );
        CompetitionPhase newPhase = mapper.map(phaseDto, CompetitionPhase.class);
        newPhase.setCompetition(competition);

        if(LocalDate.now().isBefore(newPhase.getPeriod().getEndDate())) {
            newPhase.setStatus(PhaseStatus.CONFIGURATION);
        } else if(LocalDate.now().isAfter(newPhase.getPeriod().getEndDate())) {
            newPhase.close();
        } else {
            newPhase.open();
        }
        return phaseRepository.save(newPhase);
    }

    public CompetitionPhase update(long id, CompetitionPhaseRequestDto phaseDto) {
        CompetitionPhase existingPhase = findById(id);
        mapper.map(phaseDto, existingPhase);
        return phaseRepository.save(existingPhase);
    }

    public void deleteById(long id) {
        phaseRepository.deleteById(id);
    }

    public List<CompetitionPhase> getPhasesByCompetitionId(Long competitionId) {
        return phaseRepository.findByCompetitionId(competitionId);
    }

    @Transactional
    public List<ApplicationResponseDto> importDgesData(Long competitionId, Long phaseId, DgesImportRequestDto importRequest) {
        Competition competition = competitionRepository.findById(competitionId).orElseThrow(
                () -> new IllegalArgumentException("Competition not found with id: " + competitionId)
        );
        CompetitionPhase phase = phaseRepository.findById(phaseId).orElseThrow(
                () -> new IllegalArgumentException("Phase not found with id: " + phaseId)
        );

        if(!phase.getCompetition().getId().equals(competition.getId())) {
            throw new IllegalArgumentException("Phase does not belong to the specified competition");
        }

        List<ApplicationResponseDto> importedApplications = new ArrayList<>();

        for(DgesImportRequestDto.DgesPlacementDto placementDto : importRequest.getPlacements()) {
            Application application = new Application(
                    phase.getId(),
                    new ApplicantInfo(
                            placementDto.getNif(),
                            placementDto.getName()
                    ),
                    ApplicationOrigin.DGES
            );

            CourseOption enteredCourseOption = new CourseOption(
                    application,
                    placementDto.getCourseId(),
                    1,
                    CourseOptionStatus.PLACED
            );

            application.addCourseOption(enteredCourseOption);
            // Set status to SUBMITTED for imported applications, and updated the submission date
            application.submit();


            applicationRepository.save(application);
            courseOptionRepository.save(enteredCourseOption);

            ApplicationResponseDto responseDto = mapper.map(application, ApplicationResponseDto.class);
            importedApplications.add(responseDto);
        }

        String schoolYear = importRequest.getSchoolYear();
        // Send message to RabbitMQ for further processing
        for(DgesImportRequestDto.DgesPlacementDto placementDto : importRequest.getPlacements()) {
            CreateEnrollmentMessage message = CreateEnrollmentMessage.builder()
                    .name(placementDto.getName())
                    .nif(placementDto.getNif())
                    .courseId(placementDto.getCourseId())
                    .schoolYear(schoolYear)
                    .build();
            rabbitTemplate.convertAndSend(
                    MQConfig.EXCHANGE,
                    MQConfig.ROUTING_KEY,
                    message
            );
        }

        return importedApplications;
    }
}
