package com.academins.academins.controllers;

import com.academins.academins.dto.request.AcademyClassRequestDTO;
import com.academins.academins.dto.request.StudentsToEnrollRequestDto;
import com.academins.academins.dto.response.AcademyClassResponseDTO;
import com.academins.academins.dto.response.SchoolYearResponseDTO;
import com.academins.academins.dto.response.StudentResponseDTO;
import com.academins.academins.entities.AcademyClass;
import com.academins.academins.services.AcademyClassService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing class-related operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/academins/admin/classes")
public class AdminClassController {
    private final ModelMapper mapper;
    private final AcademyClassService academyClassService;

    public AdminClassController(ModelMapper mapper, AcademyClassService academyClassService) {
        this.mapper = mapper;
        this.academyClassService = academyClassService;
    }

    /**
     * Create a new class.
     *
     * @param academyClass The class details.
     * @return ResponseEntity containing the created class and HTTP status.
     */
    @PostMapping("/create-class")
    public ResponseEntity<AcademyClassResponseDTO> createClass(@RequestBody AcademyClassRequestDTO academyClass) {
        log.info("Received request to create class: {}", academyClass);

        AcademyClass createdClass = academyClassService.createAcademyClass(academyClass);
        AcademyClassResponseDTO responseDTO = mapper.map(createdClass, AcademyClassResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Enroll students in a specific class.
     *
     * @param classId    The ID of the class.
     * @param requestDto The request DTO containing student to enroll and respective UC and schoolYear info.
     * @return ResponseEntity with HTTP status.
     */
    @PostMapping("/{classId}/enroll-students-in-classes")
    public ResponseEntity<AcademyClassResponseDTO> enrollStudentInClass(@PathVariable Long classId, @RequestBody StudentsToEnrollRequestDto requestDto) {
        log.info("Received request to enroll students in class: {}", classId);
        AcademyClass academyClass = academyClassService.enrollStudentsInClasses(classId, requestDto);
        AcademyClassResponseDTO responseDTO = mapper.map(academyClass, AcademyClassResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Get all students enrolled in a specific class.
     *
     * @param classId The ID of the class.
     * @return ResponseEntity containing the list of students and HTTP status.
     */
    @GetMapping("/{classId}/students")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsInClass(@PathVariable Long classId) {
        log.info("Received request to get students in class: {}", classId);

        List<StudentResponseDTO> students = academyClassService.getStudentsInClass(classId).stream()
                .map(student -> mapper.map(student, StudentResponseDTO.class))
                .toList();
        return ResponseEntity.ok(students);
    }
}
