package com.domus.schedules.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomEquipmentResponseDTO {
    private Long id;
    private Long classroomId;
    private Long equipmentId;
    private Integer quantity;
    private String observations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

