package com.domus.schedules.dto.response;

import com.domus.schedules.dto.ClassroomLocationDTO;
import com.domus.schedules.valueObjects.ClassroomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomResponseDTO {
    public Long id;
    public ClassroomLocationDTO location;
    public ClassroomType type;
    public Integer capacity;
    public Long schoolId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

