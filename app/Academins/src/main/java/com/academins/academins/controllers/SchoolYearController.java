package com.academins.academins.controllers;

import com.academins.academins.dto.response.SchoolYearResponseDTO;
import com.academins.academins.services.SchoolYearService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/academins/schoolyear")
public class SchoolYearController {
    private final ModelMapper mapper;
    private final SchoolYearService schoolYearService;

    public SchoolYearController(ModelMapper mapper, SchoolYearService schoolYearService) {
        this.mapper = mapper;
        this.schoolYearService = schoolYearService;
    }

    /**
     * Get school year by ID.
     *
     * @param id School year ID.
     * @return ResponseEntity containing the School Year DTO and HTTP status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SchoolYearResponseDTO> getSchoolYearById(@PathVariable Long id) {
        log.info("Getting school year for id: {}", id);

        var schoolYear = schoolYearService.getSchoolYearById(id);
        var responseDTO = mapper.map(schoolYear, SchoolYearResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }
}
