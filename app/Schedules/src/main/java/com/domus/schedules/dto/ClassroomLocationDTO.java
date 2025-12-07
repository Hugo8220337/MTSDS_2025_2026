package com.domus.schedules.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomLocationDTO {
    private String building;
    private Integer floor;
    private String roomNumber;
}

