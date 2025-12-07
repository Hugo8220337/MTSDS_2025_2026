package com.domus.applications.dto.request;

import com.domus.applications.valueObjects.CourseOptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequestDto {
    private CourseOptionStatus status;
}
