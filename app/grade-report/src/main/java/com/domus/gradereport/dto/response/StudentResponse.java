package com.domus.gradereport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
    private Long studentId;
    private Long userId;
    private String studentNumber;
    private String courseCode;
    private String studentState;
    private String fullName;
    private String dateOfBirth;
    private String fin;
    private String telephoneNumber;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
