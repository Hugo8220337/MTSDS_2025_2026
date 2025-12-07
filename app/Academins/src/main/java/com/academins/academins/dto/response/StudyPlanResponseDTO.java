package com.academins.academins.dto.response;

import com.academins.academins.entities.Course;
import com.academins.academins.entities.CurricularUnit;
import com.academins.academins.entities.School;
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
public class StudyPlanResponseDTO {
    private Long id;
    private CourseResponseDTO course;
    private SchoolResponseDTO school;
    private CurricularUnitResponseDTO curricularUnitId;
    private int curricularYear;
    private short semester;
}
