package com.domus.enrollments.dto.response;

import com.domus.enrollments.valueObjects.EnrollmentState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Enrollment entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponseDTO {

    private Long enrollmentId;
    private String nif;
    private String name;
    private Long courseId;
    private String schoolYear;
    private Long applicationId;
    private EnrollmentState enrollmentState;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
