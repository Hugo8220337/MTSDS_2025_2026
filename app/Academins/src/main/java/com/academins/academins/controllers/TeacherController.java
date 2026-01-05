package com.academins.academins.controllers;

import com.academins.academins.dto.request.EnrollStudentsInClassRequestDto;
import com.academins.academins.dto.response.StudentClassCuResponseDTO;
import com.academins.academins.dto.response.StudentResponseDTO;
import com.academins.academins.entities.StudentClassCU;
import com.academins.academins.services.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/academins/teachers/")
public class TeacherController {
    private final ModelMapper mapper;
    private final TeacherService teacherService;

    public TeacherController(ModelMapper mapper, TeacherService teacherService) {
        this.mapper = mapper;
        this.teacherService = teacherService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getTeacherById(@PathVariable Long id) {
        log.info("getTeacherById: {}", id);
        var teacher = teacherService.getTeacherById(id);
        var responseDTO = mapper.map(teacher, StudentResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/me/curricular-units/{codeCU}/students")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsInCurricularUnit(
            @RequestParam Long teacherId, // TODO remove after IAM integration
            @PathVariable String codeCU
    ) {
        List<StudentResponseDTO> students = teacherService.getStudentsInCurricularUnit(teacherId, codeCU)
                .stream()
                .map(student -> mapper.map(student, StudentResponseDTO.class))
                .toList();
        return ResponseEntity.ok(students);
    }

    @GetMapping(value = "/me/curricular-units/{codeCU}/students", params = "schoolYear")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsInCurricularUnitByYear(
            @RequestParam Long teacherId, // TODO remove after IAM integration
            @PathVariable String codeCU,
            @RequestParam String schoolYear
    ) {
        log.info("Getting students for UC {} and Year {}", codeCU, schoolYear);
        List<StudentResponseDTO> students = teacherService.getStudentsInCurricularUnit(teacherId, codeCU, schoolYear)
                .stream()
                .map(student -> mapper.map(student, StudentResponseDTO.class))
                .toList();
        return ResponseEntity.ok(students);
    }

    @PostMapping("/me/curricular-units/{codeCU}/enroll-students")
    public ResponseEntity<List<StudentClassCuResponseDTO>> enrollStudentsInCurricularUnit(
            @RequestParam Long teacherId, // TODO remove after IAM integration
            @PathVariable String codeCU,
            @RequestBody EnrollStudentsInClassRequestDto enrollRequest
    ) {
        List<StudentClassCuResponseDTO> studentClassCU = teacherService.enrollStudentsInCurricularUnit(
                        teacherId,
                        codeCU,
                        enrollRequest
                ).stream()
                .map(sc -> mapper.map(sc, StudentClassCuResponseDTO.class))
                .toList();

        return ResponseEntity.ok(studentClassCU);
    }
}
