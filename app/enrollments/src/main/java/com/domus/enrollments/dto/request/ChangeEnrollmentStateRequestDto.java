package com.domus.enrollments.dto.request;

import com.domus.enrollments.valueObjects.EnrollmentState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChangeEnrollmentStateRequestDto {
    private EnrollmentState newState;
}
