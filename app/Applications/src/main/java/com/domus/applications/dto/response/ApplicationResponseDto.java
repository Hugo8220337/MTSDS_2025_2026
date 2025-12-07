package com.domus.applications.dto.response;

import com.domus.applications.valueObjects.ApplicantInfo;
import com.domus.applications.valueObjects.ApplicationOrigin;
import com.domus.applications.valueObjects.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponseDto {
    public Long id;
    public Long phaseId;
    public ApplicantInfo applicantInfo;
    public ApplicationStatus status;
    public ApplicationOrigin origin;
    public LocalDateTime submissionDate;
    public List<CourseOptionResponseDto> courseOptions;
    public List<DocumentResponseDto> documents;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
