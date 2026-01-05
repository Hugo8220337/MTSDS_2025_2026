package com.domus.enrollments.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Enrollment entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequestDTO {
    private String name;
    private String nif;
    private Long courseId;
    private Long applicationId;
    private String schoolYear;
}
