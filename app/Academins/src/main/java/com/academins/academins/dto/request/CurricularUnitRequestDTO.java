package com.academins.academins.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Curricular Unit
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurricularUnitRequestDTO {
    private String name;
    private String codeCU;
    private int credits;
}
