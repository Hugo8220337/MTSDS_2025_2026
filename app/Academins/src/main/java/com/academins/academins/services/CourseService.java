package com.academins.academins.services;

import com.academins.academins.dto.request.CourseRequestDTO;
import com.academins.academins.dto.request.StudyPlanRequestDTO;
import com.academins.academins.dto.response.CourseResponseDTO;
import com.academins.academins.entities.Course;
import com.academins.academins.entities.CurricularUnit;
import com.academins.academins.entities.School;
import com.academins.academins.entities.StudyPlan;
import com.academins.academins.repositories.CourseRepository;
import com.academins.academins.repositories.CurricularUnitsRepository;
import com.academins.academins.repositories.SchoolRepository;
import com.academins.academins.repositories.StudyPlanRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final ModelMapper mapper;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(ModelMapper mapper, CourseRepository courseRepository) {
        this.mapper = mapper;
        this.courseRepository = courseRepository;
    }

    /**
     * Create a new Course
     *
     * @param request Data Transfer Object containing details of the Course to be created
     * @return The created Course DTO
     */
    public Course createCourse(CourseRequestDTO request) {
        Course course = mapper.map(request, Course.class);
        return courseRepository.save(course);
    }

    /**
     * Get all Courses
     *
     * @return List of Course DTOs
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Get a Course by its ID
     *
     * @param id The ID of the Course
     * @return An Optional containing the Course DTO if found, otherwise empty
     */
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Course with ID " + id + " does not exist.")
        );
    }

    /**
     * Update an existing Course
     *
     * @param id      The ID of the Course to be updated
     * @param request Data Transfer Object containing updated details of the Course
     * @return The updated Course DTO
     * @throws IllegalArgumentException if the Course with the given ID does not exist
     */
    public Course updateCourse(Long id, CourseRequestDTO request) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course with ID " + id + " does not exist."));

        mapper.map(request, existingCourse);
        return courseRepository.save(existingCourse);
    }

    /**
     * Delete a Course by its ID
     *
     * @param id The ID of the Course to be deleted
     * @throws IllegalArgumentException if the Course with the given ID does not exist
     */
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException("Course with ID " + id + " does not exist.");
        }
        courseRepository.deleteById(id);
    }

    /**
     * Get a Course by its acronym
     *
     * @param code The acronym of the Course
     * @return An Optional containing the Course if found, otherwise empty
     */
    public Course getCourseByAcronym(String code) {
        return courseRepository.findByCourseCode(code).orElseThrow(
                () -> new EntityNotFoundException("Course with code " + code + " not found.")
        );
    }
}
