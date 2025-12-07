package com.academins.academins.services;

import com.academins.academins.dto.request.StudyPlanRequestDTO;
import com.academins.academins.entities.*;
import com.academins.academins.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing study plans.
 */
@Service
public class StudyPlanService {

    private final ModelMapper mapper;
    private final StudyPlanRepository studyPlanRepository;
    private final CourseRepository courseRepository;
    private final SchoolRepository schoolRepository;
    private final CurricularUnitsRepository curricularUnitsRepository;
    private final SchoolYearRepository schoolYearRepository;


    @Autowired
    public StudyPlanService(ModelMapper mapper, StudyPlanRepository studyPlanRepository, CourseRepository courseRepository, SchoolRepository schoolRepository, CurricularUnitsRepository curricularUnitsRepository, SchoolYearRepository schoolYearRepository) {
        this.mapper = mapper;
        this.studyPlanRepository = studyPlanRepository;
        this.courseRepository = courseRepository;
        this.schoolRepository = schoolRepository;
        this.curricularUnitsRepository = curricularUnitsRepository;
        this.schoolYearRepository = schoolYearRepository;
    }

    /**
     * Create a study plan for a specific course
     *
     * @param courseCode The code of the Course
     * @throws IllegalArgumentException if the Course with the given code does not exist
     */
    public StudyPlan createStudyPlanForCourse(String courseCode, StudyPlanRequestDTO studyPlanRequestDTO) {
        // Verify that the Course exists
        Course course = courseRepository.findByCourseCode(courseCode).
                orElseThrow(() ->
                        new IllegalArgumentException("Course with ID " + courseCode + " does not exist."));

        // Verify that the School exists
        School school = schoolRepository.findById(studyPlanRequestDTO.getSchoolId()).orElseThrow(
                () -> new IllegalArgumentException("School with ID " + studyPlanRequestDTO.getSchoolId() + " does not exist.")
        );

        // Verify that the Curricular Unit exists
        CurricularUnit curricularUnit = curricularUnitsRepository.findById(studyPlanRequestDTO.getCurricularUnitId()).orElseThrow(
                () -> new IllegalArgumentException("Curricular Unit with ID " + studyPlanRequestDTO.getCurricularUnitId() + " does not exist.")
        );

        StudyPlan studyPlan = mapper.map(studyPlanRequestDTO, StudyPlan.class);
        studyPlan.setCourse(course);
        studyPlan.setSchool(school);
        studyPlan.setCurricularUnit(curricularUnit);

        return studyPlanRepository.save(studyPlan);
    }

    /**
     * Get all Study Plans
     *
     * @return List of Study Plan entities
     */
    public List<StudyPlan> getAllStudyPlans() {
        return studyPlanRepository.findAll();
    }

    /**
     * Get a Study Plan by its ID
     *
     * @param id The ID of the Study Plan
     * @return The Study Plan entity
     */
    public StudyPlan getStudyPlanById(Long id) {
        return studyPlanRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Study Plan with ID " + id + " does not exist.")
        );
    }



    /**
     * Delete a Study Plan by its ID
     *
     * @param id The ID of the Study Plan to be deleted
     * @throws IllegalArgumentException if the Study Plan with the given ID does not exist
     */
    public void deleteStudyPlan(Long id) {
        if (!studyPlanRepository.existsById(id)) {
            throw new IllegalArgumentException("Study Plan with ID " + id + " does not exist.");
        }
        studyPlanRepository.deleteById(id);
    }


    /**
     * Update an existing Study Plan for a specific course
     *
     * @param courseCode The code of the Course
     * @param studyPlanRequestDTO Data Transfer Object containing updated details of the Study Plan
     * @return The updated Study Plan entity
     * @throws IllegalArgumentException if the Course with the given code does not exist
     */
    public StudyPlan updateStudyPlan(Long studyPlanId, String courseCode, StudyPlanRequestDTO studyPlanRequestDTO) {
        StudyPlan existing = studyPlanRepository.findById(studyPlanId)
                .orElseThrow(() -> new IllegalArgumentException("Study Plan with ID " + studyPlanId + " does not exist."));

        Course course = courseRepository.findByCourseCode(courseCode).
                orElseThrow(() ->
                        new IllegalArgumentException("Course with ID " + courseCode + " does not exist."));

        School school = schoolRepository.findById(studyPlanRequestDTO.getSchoolId()).orElseThrow(
                () -> new IllegalArgumentException("School with ID " + studyPlanRequestDTO.getSchoolId() + " does not exist.")
        );

        CurricularUnit curricularUnit = curricularUnitsRepository.findById(studyPlanRequestDTO.getCurricularUnitId()).orElseThrow(
                () -> new IllegalArgumentException("Curricular Unit with ID " + studyPlanRequestDTO.getCurricularUnitId() + " does not exist.")
        );

        SchoolYear schoolYear = schoolYearRepository.findById(studyPlanRequestDTO.getCurricularYear()).orElseThrow(
                () -> new IllegalArgumentException("School Year with ID " + studyPlanRequestDTO.getCurricularYear() + " does not exist.")
        );

        if(existing.getCourse() != null && !existing.getCourse().getCourseCode().equals(course.getCourseCode())) {
            throw new IllegalArgumentException("Cannot change the course of an existing study plan.");
        }

        mapper.map(studyPlanRequestDTO, existing);

        existing.setCourse(course);
        existing.setSchool(school);
        existing.setCurricularUnit(curricularUnit);

        return studyPlanRepository.save(existing);
    }

}
