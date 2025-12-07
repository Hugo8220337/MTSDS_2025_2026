package com.academins.academins.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignCurricularUnitsToTeacherRequestDto {
    private Long schoolYearId;
    private List<Long> curricularUnitIds;
}
