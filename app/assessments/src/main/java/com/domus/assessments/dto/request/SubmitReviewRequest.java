package com.domus.assessments.dto.request;

import lombok.Data;

@Data
public class SubmitReviewRequest {
    private Long gradeId;
    private String justification;
}