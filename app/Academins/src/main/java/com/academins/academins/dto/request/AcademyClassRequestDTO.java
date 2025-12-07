package com.academins.academins.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for AcademyClass entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcademyClassRequestDTO {
    private Long schoolYearId;
    private String classCode;
    private int maxVacancies;
}
