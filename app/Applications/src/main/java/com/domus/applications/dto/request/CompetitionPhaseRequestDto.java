package com.domus.applications.dto.request;

import com.domus.applications.valueObjects.ApplicationPeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionPhaseRequestDto {
    private Integer phaseNumber;

    private ApplicationPeriod period;

    private Integer totalVacancies;
}
