package com.academins.academins.dto.response;

import com.academins.academins.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for AcademyClass entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcademyClassResponseDTO {
    private Long classId;
    private SchoolYearResponseDTO schoolYear;
    private String classCode;
    private int maxVacancies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
