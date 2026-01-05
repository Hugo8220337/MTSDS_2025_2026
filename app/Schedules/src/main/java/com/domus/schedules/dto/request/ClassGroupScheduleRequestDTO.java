package com.domus.schedules.dto.request;

import com.domus.schedules.dto.AcademicPeriodDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassGroupScheduleRequestDTO {
    private Long classGroupId;
    private Long courseUnitId;
    private Long academicYearId;
    private AcademicPeriodDTO academicPeriod;
}

