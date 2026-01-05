package com.academins.academins.controllers;

import com.academins.academins.dto.response.AcademyClassResponseDTO;
import com.academins.academins.entities.AcademyClass;
import com.academins.academins.services.AcademyClassService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/academins/classes")
public class ClassController {
    private final ModelMapper mapper;
    private final AcademyClassService academyClassService;

    public ClassController(ModelMapper mapper, AcademyClassService academyClassService) {
        this.mapper = mapper;
        this.academyClassService = academyClassService;
    }

    @GetMapping
    public ResponseEntity<List<AcademyClassResponseDTO>> getAcademyClasses() {
        log.info("Received request to get all academy classes");

        List<AcademyClassResponseDTO> classes = academyClassService.getAllAcademyClasses().stream()
                .map(academyClass -> mapper.map(academyClass, AcademyClassResponseDTO.class))
                .toList();
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademyClassResponseDTO> getAcademyClassById(@PathVariable Long id) {
        log.info("Received request to get academy class with id: {}", id);

        AcademyClass academyClass = academyClassService.getAcademyClassById(id);
        AcademyClassResponseDTO responseDTO = mapper.map(academyClass, AcademyClassResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }
}
