package com.domus.schedules.dto.request;

import com.domus.schedules.valueObjects.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleExceptionRequestDTO {
    private Long lessonId;
    private LocalDate date;
    private ExceptionType type;
    private Long alternativeClassroomId;
    private LocalTime alternativeStartTime;
    private LocalTime alternativeEndTime;
    private Long substituteInstructorId;
    private String observations;
}

