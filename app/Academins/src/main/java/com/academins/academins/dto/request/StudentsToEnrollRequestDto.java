package com.academins.academins.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentsToEnrollRequestDto {
    private Long schoolYearId;
    private Long academyClassId;
    private Long curricularUnitId;
    private List<Long> studentsIds;
}
