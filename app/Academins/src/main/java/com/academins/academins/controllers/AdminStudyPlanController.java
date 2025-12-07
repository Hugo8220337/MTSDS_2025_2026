package com.academins.academins.controllers;

import com.academins.academins.dto.request.StudyPlanRequestDTO;
import com.academins.academins.dto.response.StudentResponseDTO;
import com.academins.academins.dto.response.StudyPlanResponseDTO;
import com.academins.academins.entities.StudyPlan;
import com.academins.academins.services.StudyPlanService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/academins/admin")
public class AdminStudyPlanController {
    // TODO do (づ｡◕‿‿◕｡)づ

    private final ModelMapper mapper;
    private final StudyPlanService studyPlanService;

    public AdminStudyPlanController(ModelMapper mapper, StudyPlanService studyPlanService) {
        this.mapper = mapper;
        this.studyPlanService = studyPlanService;
    }

    /**
     * Create a study plan for a specific course.
     *
     * @param courseCode The code of the course.
     * @return ResponseEntity with HTTP status.
     */
    @PostMapping("/courses/{courseCode}/create-study-plan")
    public ResponseEntity<StudyPlanResponseDTO> createStudyPlanForCourse(
            @PathVariable String courseCode,
            @RequestBody StudyPlanRequestDTO studyPlanRequestDTO
    ) {
        log.info("Creating study plan for course {}", courseCode);
        StudyPlan studyPlan = studyPlanService.createStudyPlanForCourse(courseCode, studyPlanRequestDTO);
        StudyPlanResponseDTO studyPlanResponseDTO = mapper.map(studyPlan, StudyPlanResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(studyPlanResponseDTO);
    }

    /**
     * Get all study plans
     *
     * @return ResponseEntity containing the list of Study Plan DTOs and HTTP status
     */
    @GetMapping("/study-plans")
    public ResponseEntity<List<StudyPlanResponseDTO>> getAllStudyPlans() {
        log.info("Getting all students");
        List<StudyPlanResponseDTO> studyPlans = studyPlanService.getAllStudyPlans().stream()
                .map(studyPlan -> mapper.map(studyPlan, StudyPlanResponseDTO.class))
                .toList();
        return ResponseEntity.ok(studyPlans);
    }

    /**
     * Get a study plan by its ID
     *
     * @param id The ID of the study plan
     * @return ResponseEntity containing the Study Plan DTO if found, otherwise error message
     */
    @GetMapping("/study-plans/{id}")
    public ResponseEntity<StudyPlanResponseDTO> getStudyPlanById(@PathVariable Long id) {
        log.info("Getting study plan with id: {}", id);
        StudyPlan studyPlan = studyPlanService.getStudyPlanById(id);
        StudyPlanResponseDTO studyPlanDTO = mapper.map(studyPlan, StudyPlanResponseDTO.class);
        return ResponseEntity.ok(studyPlanDTO);
    }


    /**
     * Update an existing study plan
     *
     * @param id                  The ID of the study plan
     * @param courseCode          The code of the course
     * @param studyPlanRequestDTO Data Transfer Object containing details of the Study Plan to be updated
     * @return ResponseEntity containing the updated Study Plan DTO and HTTP status
     */
    @PutMapping("/courses/{courseCode}/study-plans/{id}")
    public ResponseEntity<StudyPlanResponseDTO> updateStudyPlan(
            @PathVariable Long id,
            @PathVariable String courseCode,
            @RequestBody StudyPlanRequestDTO studyPlanRequestDTO
    ) {
        log.info("Updating study plan with id: {} for course: {}", id, courseCode);
        StudyPlan updatedStudyPlan = studyPlanService.updateStudyPlan(id, courseCode, studyPlanRequestDTO);
        StudyPlanResponseDTO studyPlanResponseDTO = mapper.map(updatedStudyPlan, StudyPlanResponseDTO.class);
        return ResponseEntity.ok(studyPlanResponseDTO);
    }

    /**
     * Delete a study plan by its ID
     *
     * @param id The ID of the study plan to be deleted
     * @return ResponseEntity with HTTP status
     */
    @DeleteMapping("/study-plans/{id}")
    public ResponseEntity<?> deleteStudyPlan(@PathVariable Long id) {
        log.info("Deleting study plan with id: {}", id);
        studyPlanService.deleteStudyPlan(id);
        return ResponseEntity.noContent().build();
    }
}
