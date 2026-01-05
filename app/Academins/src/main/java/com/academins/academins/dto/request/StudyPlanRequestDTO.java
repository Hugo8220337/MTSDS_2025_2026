package com.academins.academins.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for StudyPlan entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyPlanRequestDTO {
    private Long courseId;
    private Long schoolId;
    private Long curricularUnitId;
    private Long curricularYear;
    private short semester;
}
