package com.domus.applications.dto.request;

import com.domus.applications.valueObjects.ApplicationPeriod;
import com.domus.applications.valueObjects.ApplicationProcessStatus;
import com.domus.applications.valueObjects.ApplicationProcessType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionRequestDto {
    private String name;

    private ApplicationProcessStatus status;

    private ApplicationProcessType type;

    private String academicYear;

    private ApplicationPeriod period;
}
