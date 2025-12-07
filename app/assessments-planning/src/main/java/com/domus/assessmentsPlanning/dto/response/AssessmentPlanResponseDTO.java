package com.domus.assessmentsPlanning.dto.response;

import com.domus.assessmentsPlanning.dto.external.ExternalDTOs.*;
import com.domus.assessmentsPlanning.model.mongo.PlanContent;
import com.domus.assessmentsPlanning.model.postgres.AssessmentComponent;
import lombok.Data;
import java.util.List;

@Data
public class AssessmentPlanResponseDTO {
    private Long planId;
    private String academicYear;
    private String status;
    
    private CourseUnitDTO courseUnit; 
    private List<TeamMemberDTO> team; 

    private List<AssessmentComponent> components;
    
    private PlanContent content;

    @Data
    public static class TeamMemberDTO {
        private ProfessorDTO professor; 
        private String role;           
    }
}