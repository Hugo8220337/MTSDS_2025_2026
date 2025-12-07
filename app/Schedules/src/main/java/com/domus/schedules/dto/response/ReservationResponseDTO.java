package com.domus.schedules.dto.response;

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
public class ReservationResponseDTO {
    private Long id;
    private ClassroomResponseDTO classroom;
    private String title;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long responsibleId;
    private String observations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

