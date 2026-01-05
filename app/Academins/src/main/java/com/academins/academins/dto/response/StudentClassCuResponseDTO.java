package com.academins.academins.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for StudentClass entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentClassCuResponseDTO {
    private Long id;
    private SchoolResponseDTO schoolYear;
    private StudentResponseDTO student;
    private AcademyClassResponseDTO academyClass;
    private CurricularUnitResponseDTO curricularUnit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
