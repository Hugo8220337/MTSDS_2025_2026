package com.domus.applications.services;

import com.domus.applications.configs.MQConfig;
import com.domus.applications.dto.request.DgesImportRequestDto;
import com.domus.applications.dto.request.SubmitApplicationRequestDto;
import com.domus.applications.dto.request.UpdateApplicationStatusRequestDto;
import com.domus.applications.entities.Application;
import com.domus.applications.entities.Competition;
import com.domus.applications.entities.CompetitionPhase;
import com.domus.applications.entities.CourseOption;
import com.domus.applications.messages.CreateEnrollmentMessage;
import com.domus.applications.repositories.CompetitionPhaseRepository;
import com.domus.applications.repositories.ApplicationRepository;
import com.domus.applications.repositories.CourseOptionRepository;
import com.domus.applications.valueObjects.ApplicationStatus;
import com.domus.applications.valueObjects.CourseOptionStatus;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {
    private final ModelMapper mapper;
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationRepository applicationRepository;
    private final CompetitionPhaseRepository competitionPhaseRepository;
    private final CompetitionService competitionService;
    private final CompetitionPhaseService competitionPhaseService;

    public ApplicationService(ModelMapper mapper, RabbitTemplate rabbitTemplate, ApplicationRepository applicationRepository, CompetitionPhaseRepository competitionPhaseRepository, CompetitionService competitionService, CompetitionPhaseService competitionPhaseService) {
        this.mapper = mapper;
        this.rabbitTemplate = rabbitTemplate;
        this.applicationRepository = applicationRepository;
        this.competitionPhaseRepository = competitionPhaseRepository;
        this.competitionService = competitionService;
        this.competitionPhaseService = competitionPhaseService;
    }

    public Application createDraftApplication(SubmitApplicationRequestDto draftApplicationDto) {
        competitionPhaseRepository.findById(draftApplicationDto.getPhaseId()).orElseThrow(
                () -> new EntityNotFoundException("Application Phase not found with id: " + draftApplicationDto.getPhaseId())
        );

        Application application = mapper.map(draftApplicationDto, Application.class);
        application.saveAsDraft();
        return applicationRepository.save(application);
    }

    public Application submitApplication(Long applicationId) {
        Application application = getApplicationById(applicationId);
        application.submit();
        return applicationRepository.save(application);
    }

    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Application not found with id: " + id));
    }

    public Application updateApplicationState(Long id, UpdateApplicationStatusRequestDto newStatus) {
        Application application = getApplicationById(id);
        CompetitionPhase competitionPhase = competitionPhaseService.findById(application.getPhaseId());
        Competition competition = competitionService.getCompetitionById(competitionPhase.getCompetition().getId());

        // Update the application status
        application.setStatus(newStatus.getStatus());
        Application saved = applicationRepository.save(application);

        // If the new status is not APPROVED, no further action is needed.
        if (newStatus.getStatus() != ApplicationStatus.APPROVED) {
            return saved;
        }

        // If there are no course options, return early.
        if (saved.getCourseOptions() == null || saved.getCourseOptions().isEmpty()) {
            return saved;
        }

        // When application becomes APPROVED, publish messages for approved course options.
        for (CourseOption option : saved.getCourseOptions()) {
            if (option.getStatus() == CourseOptionStatus.APPROVED) {
                CreateEnrollmentMessage message = CreateEnrollmentMessage.builder()
                        .name(saved.getApplicantInfo().getFullName())
                        .nif(saved.getApplicantInfo().getNif())
                        .courseId(option.getCourseId())
                        .applicationId(application.getId())
                        .schoolYear(competition.getAcademicYear())
                        .build();
                rabbitTemplate.convertAndSend(MQConfig.EXCHANGE, MQConfig.ROUTING_KEY, message);
            }
        }

        return saved;
    }

    public Application updateDraftApplication(Long id, SubmitApplicationRequestDto draftApplicationDto) {
        Application existingApplication = getApplicationById(id);
        if (existingApplication.getStatus() != ApplicationStatus.DRAFT) {
            throw new IllegalArgumentException("Cannot update already submitted application");
        }
        mapper.map(draftApplicationDto, existingApplication);
        return applicationRepository.save(existingApplication);
    }
}
