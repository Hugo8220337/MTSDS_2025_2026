package com.academins.academins.dto.request;

import com.academins.academins.valueObjects.Degree;
import com.academins.academins.valueObjects.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Course entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestDTO {
    private String name;
    private String courseCode;
    private Degree degree;
    private Type type;
}
