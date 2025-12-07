package com.domus.schedules.dto.response;

import com.domus.schedules.dto.OccupationType;
import com.domus.schedules.dto.TimeSlotDTO;
import com.domus.schedules.valueObjects.ClassroomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomOccupationResponseDTO {
    private Long schoolId;
    private LocalDate date;
    private DayOfWeek dayOfWeek;
    private ClassroomDetailDTO[] classrooms;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClassroomOccupationDetailDTO {
        private OccupationType type;
        private Integer capacity;
        private TimeSlotDTO timeSlot;
        private String teacherId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClassroomDetailDTO {
        private Long id;
        private ClassroomType type;
        private Integer capacity;
        private ClassroomOccupationDetailDTO[] occupations;
    }
}


