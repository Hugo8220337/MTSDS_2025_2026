package com.academins.academins.controllers;


import com.academins.academins.dto.request.SchoolRequestDTO;
import com.academins.academins.dto.response.SchoolResponseDTO;
import com.academins.academins.entities.School;
import com.academins.academins.services.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing Schools by Admins.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/academins/admin/schools")
public class AdminSchoolController {

    private final ModelMapper mapper;
    private final SchoolService schoolService;

    public AdminSchoolController(SchoolService schoolService, ModelMapper mapper) {
        this.schoolService = schoolService;
        this.mapper = mapper;
    }

    /**
     * Create a new School
     *
     * @param request Data Transfer Object containing details of the School to be created
     * @return ResponseEntity containing the created School DTO and HTTP status
     */
    @PostMapping("/create-school")
    public ResponseEntity<SchoolResponseDTO> createSchool(@RequestBody SchoolRequestDTO request) {
        log.info("Creating New School: {}", request);
        School createdSchool = schoolService.createSchool(request);
        SchoolResponseDTO responseDto = mapper.map(createdSchool, SchoolResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Get all Schools
     *
     * @return ResponseEntity containing the list of School DTOs and HTTP status
     */
    @GetMapping("/")
    public ResponseEntity<List<SchoolResponseDTO>> getAllSchools() {
        log.info("Getting All Schools");
        List<SchoolResponseDTO> schools = schoolService.getAllSchools().stream()
                .map(school -> mapper.map(school, SchoolResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(schools);
    }

    /**
     * Get a School by its acronym
     *
     * @param acronym The acronym of the School
     * @return ResponseEntity containing the School DTO if found, otherwise error message
     */
    @GetMapping("/{acronym}")
    public ResponseEntity<SchoolResponseDTO> getSchoolByAcronym(@PathVariable String acronym) {
        log.info("Getting School by Acronym: {}", acronym);

        School school = schoolService.getSchoolByAcronym(acronym);
        SchoolResponseDTO responseDto = mapper.map(school, SchoolResponseDTO.class);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Update an existing School
     *
     * @param acronym The acronym of the School to update
     * @param request Data Transfer Object containing updated details of the School
     * @return ResponseEntity containing the updated School DTO if the School exists, otherwise error message
     */
    @PutMapping("/{acronym}")
    public ResponseEntity<SchoolResponseDTO> updateSchool(@PathVariable String acronym, @RequestBody SchoolRequestDTO request) {
        log.info("Updating School with acronym: {}", request);

        School updatedSchool = schoolService.updateSchool(acronym, request);
        SchoolResponseDTO responseDto = mapper.map(updatedSchool, SchoolResponseDTO.class);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Delete a School by its acronym
     *
     * @param acronym The ID of the School to be deleted
     * @return ResponseEntity with HTTP status NO CONTENT if deleted, otherwise error message
     */
    @DeleteMapping("/{acronym}")
    public ResponseEntity<?> deleteSchool(@PathVariable String acronym) {
        log.info("Deleting School with acronym: {}", acronym);

        schoolService.deleteSchool(acronym);
        return ResponseEntity.noContent().build();
    }
}

