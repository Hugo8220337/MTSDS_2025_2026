package com.academins.academins.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Teacher-CurricularUnit relationship entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherCuResponseDTO {
    private Long id;
    private TeacherResponseDTO teacher;
    private CurricularUnitResponseDTO curricularUnit;
    private SchoolYearResponseDTO schoolYear;
}
