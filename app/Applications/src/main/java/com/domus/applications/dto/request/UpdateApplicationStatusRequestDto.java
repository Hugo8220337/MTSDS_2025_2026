package com.domus.applications.dto.request;

import com.domus.applications.valueObjects.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateApplicationStatusRequestDto {
    private ApplicationStatus status;
}
