package com.domus.assessments.dto.response;

import com.domus.assessments.model.postgres.GradeReview;
import lombok.Data;
import java.time.LocalDateTime;


@Data
public class ReviewResponse {
    private Long id;
    private Long gradeId;
    private GradeReview.ReviewStatus status;
    private String studentJustification;
    private String teacherObservations;
    private LocalDateTime meetingDateTime;
    private String meetingLocation;
    private Double currentGrade;
}