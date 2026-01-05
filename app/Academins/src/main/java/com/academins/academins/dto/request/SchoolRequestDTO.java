package com.academins.academins.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for School entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolRequestDTO {
    private String name;
    private String acronym;
}
