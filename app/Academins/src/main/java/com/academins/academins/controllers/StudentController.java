package com.academins.academins.controllers;

import com.academins.academins.dto.request.StudentRequestDTO;
import com.academins.academins.dto.response.CurricularUnitResponseDTO;
import com.academins.academins.dto.response.StudentResponseDTO;
import com.academins.academins.entities.Student;
import com.academins.academins.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/academins/students")
public class StudentController {

    private final ModelMapper mapper;
    private final StudentService studentService;

    public StudentController(ModelMapper mapper, StudentService studentService) {
        this.mapper = mapper;
        this.studentService = studentService;
    }

    /**
     * Create a new student (student self-registration)
     *
     * @param studentDTO Data Transfer Object containing student details
     * @return ResponseEntity containing the created Student DTO and HTTP status
     */
    @PostMapping("/create")
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody StudentRequestDTO studentDTO) {
        log.info("Creating student {}", studentDTO);
        Student createdStudent = studentService.createStudent(studentDTO);
        StudentResponseDTO studentResponseDTO = mapper.map(createdStudent, StudentResponseDTO.class);
        return ResponseEntity.ok().body(studentResponseDTO);
    }

    /**
     * Update Student Information
     *
     * @param studentNumber The number of the student to be updated
     * @param studentDTO    Data Transfer Object containing updated student details
     */
    @PutMapping("/{studentNumber}/update")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable String studentNumber, @RequestBody StudentRequestDTO studentDTO) {
        log.info("Updating student {}", studentNumber);
        Student updatedStudent = studentService.updateStudent(studentNumber, studentDTO);
        StudentResponseDTO studentResponseDTO = mapper.map(updatedStudent, StudentResponseDTO.class);
        return ResponseEntity.ok().body(studentResponseDTO);
    }

    @GetMapping("/me/curricular-units")
    public ResponseEntity<List<CurricularUnitResponseDTO>> getCurricularUnitsForStudent(
            @RequestParam Long studentId // TODO replace after IAM implementation
    ) {
        List<CurricularUnitResponseDTO> curricularUnits = studentService.getCurricularUnitsForStudent(studentId)
                .stream()
                .map(cu -> mapper.map(cu, CurricularUnitResponseDTO.class))
                .toList();
        return ResponseEntity.ok().body(curricularUnits);
    }

}
