package com.domus.assessmentsPlanning.dto.external;

import lombok.Data;

public class ExternalDTOs {

    @Data
    public static class CourseUnitDTO {
        private Long id;
        private String name;      
        private String acronym;   
        private String degree;    
    }

    @Data
    public static class ProfessorDTO {
        private Long id;
        private String name;
        private String email;
        private String qualification;
    }
}