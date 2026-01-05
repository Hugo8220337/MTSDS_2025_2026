package com.academins.academins.controllers;

import com.academins.academins.dto.request.SchoolYearRequestDTO;
import com.academins.academins.dto.response.SchoolYearResponseDTO;
import com.academins.academins.entities.SchoolYear;
import com.academins.academins.services.SchoolYearService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/academins/admin/schoolyear")
public class AdminSchoolYearController {

    private final ModelMapper mapper;
    private final SchoolYearService schoolYearService;

    public AdminSchoolYearController(ModelMapper mapper, SchoolYearService schoolYearService) {
        this.mapper = mapper;
        this.schoolYearService = schoolYearService;
    }

    /**
     * Create a new school year.
     *
     * @param request Data Transfer Object containing school year details.
     * @return ResponseEntity containing the created School Year DTO and HTTP status.
     */
    @PostMapping("/create-school-year")
    public ResponseEntity<SchoolYearResponseDTO> createSchoolYear(@RequestBody SchoolYearRequestDTO request) {
        log.info("Creating new school year");

        SchoolYear schoolYear = schoolYearService.createSchoolYear(request);
        SchoolYearResponseDTO responseDTO = mapper.map(schoolYear, SchoolYearResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

}
