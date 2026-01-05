package com.academins.academins.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Teacher entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRequestDTO {
    private Long userId;
    private Integer workerNumber;
    private String fullName;
    private String email;
}
