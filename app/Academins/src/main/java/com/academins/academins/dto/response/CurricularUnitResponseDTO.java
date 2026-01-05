package com.academins.academins.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Curricular Unit
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurricularUnitResponseDTO {
    private Long curricularUnitId;
    private String name;
    private String codeCU;
    private int credits;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
