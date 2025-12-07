package com.academins.academins.services;

import com.academins.academins.dto.request.CourseRequestDTO;
import com.academins.academins.dto.response.CourseResponseDTO;
import com.academins.academins.dto.request.SchoolRequestDTO;
import com.academins.academins.entities.Course;
import com.academins.academins.entities.School;
import com.academins.academins.repositories.CourseRepository;
import com.academins.academins.repositories.SchoolRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Schools.
 * Provides methods to create, retrieve, update, and delete Schools.
 */
@Service
public class SchoolService {

    private final ModelMapper modelMapper;
    private final SchoolRepository schoolRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public SchoolService(ModelMapper modelMapper, SchoolRepository schoolRepository, CourseRepository courseRepository) {
        this.modelMapper = modelMapper;
        this.schoolRepository = schoolRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Get all Schools
     *
     * @return List of School DTOs
     */
    public List<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    /**
     * Get a School by its acronym
     *
     * @param acronym The acronym of the School
     * @return An Optional containing the School DTO if found, otherwise empty
     */
    public School getSchoolByAcronym(String acronym) {
        return schoolRepository.findByAcronym(acronym).orElseThrow(() -> new EntityNotFoundException("School with acronym " + acronym + " not found."));
    }

    /**
     * Create a new School
     *
     * @param schoolResponseDTO Data Transfer Object containing details of the School to be created
     * @return The created School
     * @throws IllegalArgumentException if a School with the same acronym already exists
     */
    public School createSchool(SchoolRequestDTO schoolResponseDTO) {
        if (schoolRepository.existsByAcronym(schoolResponseDTO.getAcronym())) {
            throw new IllegalArgumentException("School with acronym " + schoolResponseDTO.getAcronym() + " already exists.");
        }
        School school = modelMapper.map(schoolResponseDTO, School.class);
        return schoolRepository.save(school);
    }

    /**
     * Update an existing School
     *
     * @param acronym        The acronym of the School to be updated
     * @param request Data Transfer Object containing updated details of the School
     * @return An Optional containing the updated School DTO if the School exists, otherwise empty
     */
    public School updateSchool(String acronym, SchoolRequestDTO request) {
        School school = schoolRepository.findByAcronym(acronym).orElseThrow(
                () -> new EntityNotFoundException("School with acronym " + acronym + " not found.")
        );

        // Update fields Keeping the ID unchanged
        modelMapper.map(request, school);

        return schoolRepository.save(school);
    }


    /**
     * Delete a School by its acronym
     *
     * @param acronym The acronym of the School to be deleted
     */
    @Transactional
    public void deleteSchool(String acronym) {
        schoolRepository.deleteByAcronym(acronym);
    }


    /**
     * Get a School by its ID
     *
     * @param id The ID of the School
     * @return The School if found
     */
    public School getSchoolById(Long id) {
        return schoolRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("School with id " + id + " not found."));
    }
}
