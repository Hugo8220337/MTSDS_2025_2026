package com.domus.schedules.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomAvailabilityResponseDTO {
    private Long classroomId;
    private List<LocalDateTime> busySlots;
    private List<LocalDateTime> availableSlots;
}