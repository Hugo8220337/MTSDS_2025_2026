package com.domus.assessments.dto.request;

import lombok.Data;

@Data
public class CloseReviewRequest {
    private String observations;
    private Double newGrade;
}