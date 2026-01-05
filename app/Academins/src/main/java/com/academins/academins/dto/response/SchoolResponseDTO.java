package com.academins.academins.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for School entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolResponseDTO {
    private Long schoolId;
    private String name;
    private String acronym;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
