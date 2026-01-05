package com.academins.academins.dto.response;

import com.academins.academins.valueObjects.Degree;
import com.academins.academins.valueObjects.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Course entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseDTO {
    private Long courseId;
    private String name;
    private String courseCode;
    private Degree degree;
    private Type type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
