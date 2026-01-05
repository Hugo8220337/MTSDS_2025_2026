package com.domus.schedules.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicPeriodDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer semester;
}

