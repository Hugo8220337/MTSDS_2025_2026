package com.domus.enrollments.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateEnrollmentMessage {
    private String name;
    private String nif;
    private Long courseId;
    private Long applicationId;
    private String schoolYear;
}