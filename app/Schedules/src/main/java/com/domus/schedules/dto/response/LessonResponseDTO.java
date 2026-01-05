package com.domus.schedules.dto.response;

import com.domus.schedules.dto.TimeSlotDTO;
import com.domus.schedules.valueObjects.LessonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonResponseDTO {
    private Long id;
    private ClassGroupScheduleResponseDTO classGroupSchedule;
    private Long instructorId;
    private Long classroomId;
    private DayOfWeek dayOfWeek;
    private TimeSlotDTO timeSlot;
    private Integer durationMinutes;
    private LessonType lessonType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

