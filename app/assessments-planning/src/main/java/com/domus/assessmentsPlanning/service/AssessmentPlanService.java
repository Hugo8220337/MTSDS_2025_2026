package com.domus.assessmentsPlanning.service;

import com.domus.assessmentsPlanning.client.AcademinsApiClient;
import com.domus.assessmentsPlanning.dto.response.AssessmentPlanResponseDTO;
import com.domus.assessmentsPlanning.dto.request.CreatePlanRequestDto;
import com.domus.assessmentsPlanning.dto.external.ExternalDTOs.CourseUnitDTO;
import com.domus.assessmentsPlanning.dto.external.ExternalDTOs.ProfessorDTO;
import com.domus.assessmentsPlanning.model.mongo.PlanContent;
import com.domus.assessmentsPlanning.model.postgres.AssessmentPlan;
import com.domus.assessmentsPlanning.model.postgres.PlanStatus;
import com.domus.assessmentsPlanning.model.postgres.PlanTeamMember;
import com.domus.assessmentsPlanning.model.postgres.ProfessorRole;
import com.domus.assessmentsPlanning.repository.AssessmentPlanRepository;
import com.domus.assessmentsPlanning.repository.PlanContentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AssessmentPlanService implements AssessmentPlanServiceInterface {

    private final ModelMapper mapper;
    private final AcademinsApiClient academinsApiClient;
    private final AssessmentPlanRepository postgresRepository;
    private final PlanContentRepository mongoRepository;

    public AssessmentPlanService(ModelMapper mapper, AcademinsApiClient academinsApiClient, AssessmentPlanRepository postgresRepository, PlanContentRepository mongoRepository) {
        this.mapper = mapper;
        this.academinsApiClient = academinsApiClient;
        this.postgresRepository = postgresRepository;
        this.mongoRepository = mongoRepository;
    }

    @Override
    @Transactional
    public AssessmentPlanResponseDTO createPlan(CreatePlanRequestDto request) {
        log.info("Creating new Assessment Plan for Course ID: {}", request.getCourseUnitId());

        if (!academinsApiClient.existsTeacher(request.getRegenteId())) {
            throw new EntityNotFoundException("Regenete does not exist in Academins service");
        }

        CourseUnitDTO course = academinsApiClient.getCourseById(request.getCourseUnitId());
        if (course == null)
            throw new EntityNotFoundException("Course not found in Academins service");

        PlanContent content = new PlanContent();
        content.setGeneralObjectives("Objectives for " + course.getName());
        PlanContent savedContent = mongoRepository.save(content);

        AssessmentPlan assessmentPlan = mapper.map(request, AssessmentPlan.class);
        assessmentPlan.setStatus(PlanStatus.DRAFT);
        assessmentPlan.setContentId(savedContent.getId());

        PlanTeamMember regente = new PlanTeamMember();
        regente.setProfessorId(request.getRegenteId());
        regente.setRole(ProfessorRole.REGENT);
        regente.setAssessmentPlan(assessmentPlan);

        assessmentPlan.setTeam(new ArrayList<>());
        assessmentPlan.getTeam().add(regente);

        AssessmentPlan savedPlan = postgresRepository.save(assessmentPlan);

        return mapToDTO(savedPlan, savedContent);
    }

    @Override
    public AssessmentPlanResponseDTO getFullPlan(Long planId) {
                AssessmentPlan plan = postgresRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

                PlanContent content = mongoRepository.findById(plan.getContentId())
                .orElse(new PlanContent()); 
                return mapToDTO(plan, content);
    }

    @Override
    public void updateContent(Long planId, PlanContent content) {
        AssessmentPlan plan = postgresRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

                content.setId(plan.getContentId());
        mongoRepository.save(content);
        log.info("Updated Mongo content for Plan ID: {}", planId);
    }

    @Override
    public void submitForApproval(Long planId) {
        AssessmentPlan plan = postgresRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

                        
        plan.setStatus(PlanStatus.SUBMITTED);
        postgresRepository.save(plan);
    }

        private AssessmentPlanResponseDTO mapToDTO(AssessmentPlan plan, PlanContent content) {
        AssessmentPlanResponseDTO dto = new AssessmentPlanResponseDTO();

                dto.setPlanId(plan.getId());
        dto.setAcademicYear(plan.getAcademicYear());
        dto.setStatus(plan.getStatus().name());
        dto.setComponents(plan.getComponents());
        dto.setContent(content);

                try {
            CourseUnitDTO courseDTO = academinsApiClient.getCourseById(plan.getCourseUnitId());
            if (courseDTO == null)
                throw new EntityNotFoundException("Course not found in Academins service");
            dto.setCourseUnit(courseDTO);
        } catch (Exception e) {
            log.warn("Could not fetch Course info via Feign", e);
                        CourseUnitDTO fallback = new CourseUnitDTO();
            fallback.setId(plan.getCourseUnitId());
            fallback.setName("Unknown Course (Service Unavailable)");
            dto.setCourseUnit(fallback);
        }

                List<AssessmentPlanResponseDTO.TeamMemberDTO> teamDTOs = plan.getTeam().stream().map(member -> {
            AssessmentPlanResponseDTO.TeamMemberDTO memberDto = new AssessmentPlanResponseDTO.TeamMemberDTO();
            memberDto.setRole(member.getRole().name());

            try {
                ProfessorDTO profDto = academinsApiClient.getProfessorById(member.getProfessorId());
                if (profDto == null)
                    throw new EntityNotFoundException("Professor not found in Academins service");
                memberDto.setProfessor(profDto);
            } catch (Exception e) {
                log.warn("Could not fetch Professor info", e);
                ProfessorDTO fallback = new ProfessorDTO();
                fallback.setId(member.getProfessorId());
                fallback.setName("Unknown Professor");
                memberDto.setProfessor(fallback);
            }
            return memberDto;
        }).collect(Collectors.toList());

        dto.setTeam(teamDTOs);

        return dto;
    }
}