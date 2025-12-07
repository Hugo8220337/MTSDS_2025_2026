package com.academins.academins.controllers;

import com.academins.academins.dto.response.StudentResponseDTO;
import com.academins.academins.entities.Student;
import com.academins.academins.services.StudentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing student-related operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/academins/admin/students")
public class AdminStudentController {

    private final ModelMapper mapper;
    private final StudentService studentService;

    public AdminStudentController(ModelMapper mapper, StudentService studentService) {
        this.mapper = mapper;
        this.studentService = studentService;
    }

    /**
     * Get all students
     *
     * @return ResponseEntity containing the list of Student DTOs and HTTP status
     */
    @GetMapping("/")
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        log.info("Getting all students");
        List<StudentResponseDTO> students = studentService.getAllStudents().stream()
                .map(student -> mapper.map(student, StudentResponseDTO.class))
                .toList();
        return ResponseEntity.ok(students);
    }

    /**
     * Get a student by their student number
     *
     * @param studentNumber The student number of the student
     * @return ResponseEntity containing the Student DTO if found, otherwise error message
     */
    @GetMapping("/{studentNumber}")
    public ResponseEntity<StudentResponseDTO> getStudentByStudentNumber(@PathVariable String studentNumber) {
        log.info("Getting student with student number: {}", studentNumber);
        Student student = studentService.getStudentByStudentNumber(studentNumber);
        StudentResponseDTO studentDTO = mapper.map(student, StudentResponseDTO.class);
        return ResponseEntity.ok(studentDTO);
    }

    /**
     * Get a student by their studentId
     *
     * @param studentId The student number of the student
     * @return ResponseEntity containing the Student DTO if found, otherwise error message
     */
    @GetMapping("/id/{studentId}")
    public ResponseEntity<StudentResponseDTO> getStudentByStudentId(@PathVariable Long studentId) {
        log.info("Getting student with studentId: {}", studentId);
        Student student = studentService.getStudentByStudentId(studentId);
        StudentResponseDTO studentDTO = mapper.map(student, StudentResponseDTO.class);
        return ResponseEntity.ok(studentDTO);
    }

}
