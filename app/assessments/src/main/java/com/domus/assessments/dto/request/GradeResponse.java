package com.domus.assessments.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GradeResponse {
    private Long id;
    private Long studentId;
    private Long evaluationMomentId;
    private Double gradeValue;
    private boolean isPublished;
    private LocalDateTime updatedAt;
}