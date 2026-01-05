package com.domus.assessments.dto.request;

import lombok.Data;

@Data
public class CreateGradeRequest {
    private Long studentId;
    private Long evaluationMomentId;
    private Double gradeValue;
    private Long teacherId;

}
