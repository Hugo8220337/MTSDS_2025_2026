package com.academins.academins.dto.request;

import com.academins.academins.valueObjects.StudentState;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Student entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequestDTO {
    private Long userId;
    private String studentNumber;
    private String courseCode;
    private StudentState studentState;
    private String fullName;
    private String dateOfBirth;
    private String fin;
    private String telephoneNumber;
    private String email;
}