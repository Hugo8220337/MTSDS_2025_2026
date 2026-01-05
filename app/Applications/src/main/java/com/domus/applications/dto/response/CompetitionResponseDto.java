package com.domus.applications.dto.response;

import com.domus.applications.valueObjects.ApplicationPeriod;
import com.domus.applications.valueObjects.ApplicationProcessStatus;
import com.domus.applications.valueObjects.ApplicationProcessType;
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
public class CompetitionResponseDto {
    private Long id;

    private String name;

    private ApplicationProcessType type;

    private String academicYear;

    private ApplicationPeriod period;

    private ApplicationProcessStatus status;

    private List<CompetitionPhaseResponseDto> phases;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
