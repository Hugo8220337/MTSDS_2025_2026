package com.academins.academins.controllers;

import com.academins.academins.dto.response.SchoolResponseDTO;
import com.academins.academins.entities.School;
import com.academins.academins.services.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/academins/schools")
public class SchoolController {
    private final ModelMapper mapper;
    private final SchoolService schoolService;

    public SchoolController(ModelMapper mapper, SchoolService schoolService) {
        this.mapper = mapper;
        this.schoolService = schoolService;
    }

    /**
     * Get a School by its acronym
     *
     * @param acronym The acronym of the School
     * @return ResponseEntity containing the School DTO if found, otherwise error message
     */
    @GetMapping("/{acronym}")
    public ResponseEntity<SchoolResponseDTO> getSchoolByAcronym(@PathVariable String acronym) {
        log.info("Getting school with acronym: {}", acronym);
        School school = schoolService.getSchoolByAcronym(acronym);
        SchoolResponseDTO schoolResponseDTO = mapper.map(school, SchoolResponseDTO.class);
        return ResponseEntity.ok(schoolResponseDTO);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SchoolResponseDTO> getSchoolById(@PathVariable Long id) {
        log.info("Getting school with id: {}", id);
        School school = schoolService.getSchoolById(id);
        SchoolResponseDTO schoolResponseDTO = mapper.map(school, SchoolResponseDTO.class);
        return ResponseEntity.ok(schoolResponseDTO);
    }
}
