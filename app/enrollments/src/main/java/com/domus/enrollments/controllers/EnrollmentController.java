package com.domus.enrollments.controllers;

import com.domus.enrollments.dto.request.ChangeEnrollmentStateRequestDto;
import com.domus.enrollments.dto.request.EnrollmentRequestDTO;
import com.domus.enrollments.dto.response.EnrollmentResponseDTO;
import com.domus.enrollments.entities.Enrollment;
import com.domus.enrollments.services.EnrollmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Enrollments.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {

    private final ModelMapper mapper;
    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(ModelMapper mapper, EnrollmentService enrollmentService) {
        this.mapper = mapper;
        this.enrollmentService = enrollmentService;
    }

    /**
     * Create a new Enrollment
     *
     * @param enrollmentRequest Data Transfer Object containing details of the Enrollment to be created
     * @return ResponseEntity containing the created Enrollment DTO and HTTP status
     */
    @PostMapping("/create-enrollment")
    public ResponseEntity<EnrollmentResponseDTO> createEnrollment(@RequestBody EnrollmentRequestDTO enrollmentRequest) {
        log.info("Recieved request to create enrollment: {}", enrollmentRequest);

        Enrollment enrollment = enrollmentService.createEnrollment(enrollmentRequest);
        EnrollmentResponseDTO responseDTO = mapper.map(enrollment, EnrollmentResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    /**
     * Get an Enrollment by its ID
     *
     * @param id The ID of the Enrollment
     * @return ResponseEntity containing the Enrollment DTO and HTTP status
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDTO> getEnrollmentById(@PathVariable Long id) {
        log.info("Getting enrollment with ID: {}", id);
        Enrollment enrollment = enrollmentService.findEnrollmentById(id);
        EnrollmentResponseDTO responseDTO = mapper.map(enrollment, EnrollmentResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }


    /**
     * Get all Enrollments
     *
     * @return ResponseEntity containing a list of Enrollment DTOs and HTTP status
     */
    @GetMapping("/all")
    public ResponseEntity<List<EnrollmentResponseDTO>> getAllEnrollments() {
        log.info("Getting all enrollments");
        List<EnrollmentResponseDTO> enrollments = enrollmentService.findAllEnrollments().stream()
                .map(enrollment -> mapper.map(enrollment, EnrollmentResponseDTO.class))
                .toList();
        return ResponseEntity.ok(enrollments);
    }


    /**
     * Update the state of an existing Enrollment
     *
     * @param id                The ID of the Enrollment to be updated
     * @param request Data Transfer Object containing updated details of the new Enrollment state
     * @return ResponseEntity containing the updated Enrollment DTO and HTTP status
     */
    @PutMapping("/{id}/update-state")
    public ResponseEntity<EnrollmentResponseDTO> updateEnrollmentState(@PathVariable Long id, @RequestBody ChangeEnrollmentStateRequestDto request) {
        log.info("Received request to update enrollment: {}", request);
        try {
            Enrollment enrollment = enrollmentService.updateEnrollmentState(id, request);
            EnrollmentResponseDTO responseDTO = mapper.map(enrollment, EnrollmentResponseDTO.class);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            log.error("Error updating enrollment: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
