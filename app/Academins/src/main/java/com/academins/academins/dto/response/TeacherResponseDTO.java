package com.academins.academins.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Teacher entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponseDTO {
    private Long teacherId;
    private Long userId;
    private Integer workerNumber;
    private String fullName;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
