package com.academins.academins.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EnrollStudentsInClassRequestDto {
    private Long schoolYearId;
    private Long academyClassId;
    private List<Long> studentIds;
}
