package com.domus.enrollments.dto.request;

import com.domus.enrollments.entities.Enrollment;
import com.domus.enrollments.valueObjects.DocumentState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentDocumentRequestDTO {
    private Enrollment enrollment;
    private String documentType;
    //private String documentPath;
    private String documentContent;
}
