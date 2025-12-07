package com.domus.applications.dto.response;

import com.domus.applications.valueObjects.ApplicationPeriod;
import com.domus.applications.valueObjects.PhaseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionPhaseResponseDto {
    private Long id;

    private Integer phaseNumber;

    private ApplicationPeriod period;

    private Integer totalVacancies;

    private PhaseStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

//    private CompetitionResponseDto competition; // This will lead to circular references
}
