package com.academins.academins.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolYearResponseDTO {
    private Long schoolYearId;
    private String year;
    private String startDate;
    private String endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
