package com.domus.applications.dto.request;

import com.domus.applications.valueObjects.ApplicantInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitApplicationRequestDto {
    private Long phaseId;
    private ApplicantInfo applicantInfo;
}
