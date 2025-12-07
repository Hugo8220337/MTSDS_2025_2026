package com.domus.schedules.controllers;

import com.domus.schedules.dto.response.ClassroomOccupationResponseDTO;
import com.domus.schedules.services.ClassroomOccupationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller for public classroom queries.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/schedules/schools/{schoolId}/classrooms")
public class ClassroomController {

    private final ClassroomOccupationService classroomOccupationService;

    public ClassroomController(ClassroomOccupationService classroomOccupationService) {
        this.classroomOccupationService = classroomOccupationService;
    }

    /**
     * Get occupation of all classrooms for a specific date.
     *
     * @param schoolId the school ID
     * @param date     the date to check (format: yyyy-MM-dd)
     * @return classroom occupation details
     */
    @GetMapping("/occupation")
    public ResponseEntity<ClassroomOccupationResponseDTO> getClassroomOccupation(
            @PathVariable Long schoolId,
            @RequestParam LocalDate date) {
        // TODO  funciona mas precisa de mais testes
        log.info("Getting classroom occupation for school ID {} on date {}", schoolId, date);
        ClassroomOccupationResponseDTO dto = classroomOccupationService.getOccupation(schoolId, date);
        return ResponseEntity.ok(dto);
    }
}
