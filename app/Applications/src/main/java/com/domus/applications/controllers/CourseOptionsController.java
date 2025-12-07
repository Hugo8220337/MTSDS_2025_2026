package com.domus.applications.controllers;

import com.domus.applications.dto.request.CourseOptionRequestDto;
import com.domus.applications.dto.request.UpdateStatusRequestDto;
import com.domus.applications.dto.response.CourseOptionResponseDto;
import com.domus.applications.services.CourseOtionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/applications/{applicationId}")
public class CourseOptionsController {

    private final ModelMapper mapper;
    private final CourseOtionService courseOptionService;

    public CourseOptionsController(ModelMapper mapper, CourseOtionService courseOptionService) {
        this.mapper = mapper;
        this.courseOptionService = courseOptionService;
    }

    @PostMapping("/add-course-option")
    public ResponseEntity<CourseOptionResponseDto> addCourseOption(
            @PathVariable Long applicationId,
            @RequestBody CourseOptionRequestDto courseOptionRequestDto
    ) {
        log.info("addCourseOption called");

        var createdCourseOption = courseOptionService.addCourseOption(applicationId, courseOptionRequestDto);
        var responseDto = mapper.map(createdCourseOption, CourseOptionResponseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/course-options")
    public ResponseEntity<List<CourseOptionResponseDto>> getCourseOptions(@PathVariable Long applicationId) {
        log.info("getCourseOptions called");

        var courseOptions = courseOptionService.getCourseOptionsByApplicationId(applicationId);
        var responseDtos = courseOptions.stream()
                .map(co -> mapper.map(co, CourseOptionResponseDto.class))
                .toList();
        return ResponseEntity.ok(responseDtos);
    }

    @DeleteMapping("/course-options/{courseOptionId}/remove")
    public ResponseEntity<Void> removeCourseOption(
            @PathVariable Long applicationId,
            @PathVariable Long courseOptionId
    ) {
        log.info("removeCourseOption called");

        courseOptionService.removeCourseOption(applicationId, courseOptionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/course-options/{courseOptionId}/update-status")
    public ResponseEntity<CourseOptionResponseDto> updateCourseStatus(
            @PathVariable Long applicationId,
            @PathVariable Long courseOptionId,
            @RequestBody UpdateStatusRequestDto status
    ) {
        log.info("updateCourseOption called");

        var updatedCourseOption = courseOptionService.updateCourseOption(
                applicationId,
                courseOptionId,
                status
        );
        var responseDto = mapper.map(updatedCourseOption, CourseOptionResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }
}
