package com.academins.academins.controllers;

import com.academins.academins.dto.request.CourseRequestDTO;
import com.academins.academins.dto.request.StudyPlanRequestDTO;
import com.academins.academins.dto.response.CourseResponseDTO;
import com.academins.academins.dto.response.StudyPlanResponseDTO;
import com.academins.academins.entities.Course;
import com.academins.academins.entities.StudyPlan;
import com.academins.academins.services.CourseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing course-related operations by admin.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/academins/admin/courses")
public class AdminCourseController {

    private final ModelMapper mapper;
    private final CourseService courseService;

    public AdminCourseController(ModelMapper mapper, CourseService courseService) {
        this.mapper = mapper;
        this.courseService = courseService;
    }

    /**
     * Create a new Course
     *
     * @param request Data Transfer Object containing details of the Course to be created
     * @return ResponseEntity containing the created Course DTO and HTTP status
     */
    @PostMapping("/create-course")
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseRequestDTO request) {
        log.info("Creating course with data: {}", request);

        Course createdCourse = courseService.createCourse(request);
        CourseResponseDTO courseResponseDTO = mapper.map(createdCourse, CourseResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseResponseDTO);

    }

    /**
     * Get all Courses
     *
     * @return ResponseEntity containing list of Course DTOs and HTTP status
     */
    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        log.info("Retrieving all courses from database");

        List<CourseResponseDTO> courses = courseService.getAllCourses().stream()
                .map(course -> mapper.map(course, CourseResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(courses);
    }

    /**
     * Get a Course by its ID
     *
     * @param id The ID of the Course
     * @return ResponseEntity containing the Course DTO and HTTP status
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
        log.info("Retrieving course by id: {}", id);

        Course course = courseService.getCourseById(id);
        CourseResponseDTO courseDTO = mapper.map(course, CourseResponseDTO.class);
        return ResponseEntity.ok(courseDTO);
    }

    /**
     * Update an existing Course
     *
     * @param id      The ID of the Course to be updated
     * @param request Data Transfer Object containing updated details of the Course
     * @return ResponseEntity containing the updated Course DTO and HTTP status
     */
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Long id, @RequestBody CourseRequestDTO request) {
        log.info("Updating course with data: {}", request);

        Course updatedCourse = courseService.updateCourse(id, request);
        CourseResponseDTO courseResponseDTO = mapper.map(updatedCourse, CourseResponseDTO.class);
        return ResponseEntity.ok(courseResponseDTO);
    }

    /**
     * Delete a Course by its ID
     *
     * @param id The ID of the Course to be deleted
     * @return ResponseEntity with HTTP status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.info("Deleting course with data: {}", id);

        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }

}
