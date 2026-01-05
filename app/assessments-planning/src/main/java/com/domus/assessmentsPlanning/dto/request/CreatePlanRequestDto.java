package com.domus.assessmentsPlanning.dto.request;

import lombok.Data;

@Data
public class CreatePlanRequestDto {
    private Long courseUnitId;
    private String academicYear;
    private Integer semester;
    private Long regenteId;
}