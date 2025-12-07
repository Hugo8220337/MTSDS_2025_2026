package com.domus.enrollments.dto.response;

import com.domus.enrollments.entities.Enrollment;
import com.domus.enrollments.valueObjects.DocumentState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentDocumentResponseDTO {
    private Long enrollmentDocumentId;
    private Enrollment enrollment;
    private String documentType;
    private String documentContent;

    //private String documentPath;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
