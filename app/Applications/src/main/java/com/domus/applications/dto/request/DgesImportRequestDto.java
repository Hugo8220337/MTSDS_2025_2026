package com.domus.applications.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DgesImportRequestDto {
    private String schoolYear;
    private List<DgesPlacementDto> placements;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DgesPlacementDto {
        private String name;
        private String nif;
        private Long courseId;
    }
}
