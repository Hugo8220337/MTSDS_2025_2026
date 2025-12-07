package com.academins.academins.controllers;

import com.academins.academins.dto.request.AssignCurricularUnitsToTeacherRequestDto;
import com.academins.academins.dto.request.TeacherRequestDTO;
import com.academins.academins.dto.response.TeacherCuResponseDTO;
import com.academins.academins.dto.response.TeacherResponseDTO;
import com.academins.academins.entities.Teacher;
import com.academins.academins.services.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/academins/admin/teachers")
public class AdminTeacherController {

    private final ModelMapper mapper;
    private final TeacherService teacherService;

    public AdminTeacherController(ModelMapper mapper, TeacherService teacherService) {
        this.mapper = mapper;
        this.teacherService = teacherService;
    }

    /**
     * Create a new Teacher
     *
     * @param request Data Transfer Object containing details of the Teacher to be created
     * @return ResponseEntity containing the created Teacher DTO and HTTP status
     */
    @PostMapping("/create")
    public ResponseEntity<TeacherResponseDTO> createTeacher(@RequestBody TeacherRequestDTO request) {
        log.info("Creating Teacher with data: {}", request);

        Teacher createdTeacher = teacherService.createTeacher(request);
        TeacherResponseDTO createdTeacherDTO = mapper.map(createdTeacher, TeacherResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacherDTO);
    }

    /**
     * Assign Curricular Units to a Teacher
     *
     * @param teacherNumber     The unique number of the Teacher
     * @param curricularUnitIds List of Curricular Unit IDs to be assigned to the Teacher
     * @return ResponseEntity with HTTP status OK if successful, NOT FOUND if Teacher not found
     */
    @PostMapping("/{teacherNumber}/assign-curricular-unit")
    public ResponseEntity<List<TeacherCuResponseDTO>> assignCurricularUnitToTeacher(
            @PathVariable Integer teacherNumber, @RequestBody AssignCurricularUnitsToTeacherRequestDto curricularUnitIds
    ) {
        log.info("Assigning Curricular Unit to Teacher with ID: {}", teacherNumber);
        List<TeacherCuResponseDTO> teacherCus = teacherService.assignCurricularUnitsToTeacher(teacherNumber, curricularUnitIds).stream()
                .map(cu -> mapper.map(cu, TeacherCuResponseDTO.class))
                .toList();
        return ResponseEntity.ok(teacherCus);
    }
}
