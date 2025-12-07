package com.domus.schedules.dto.request;

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
public class LessonRequestDTO {
    private Long classGroupScheduleId;
    private Long instructorId;
    private Long classroomId;
    private DayOfWeek dayOfWeek;
    private TimeSlotDTO timeSlot;
    private Integer durationMinutes;
    private LessonType lessonType;
}

