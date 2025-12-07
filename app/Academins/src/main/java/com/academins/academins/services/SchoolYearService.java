package com.academins.academins.services;

import com.academins.academins.dto.request.SchoolYearRequestDTO;
import com.academins.academins.entities.SchoolYear;
import com.academins.academins.repositories.SchoolYearRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing school years.
 *
 */
@Service
public class SchoolYearService {

    private final SchoolYearRepository schoolYearRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SchoolYearService(SchoolYearRepository schoolYearRepository, ModelMapper modelMapper) {
        this.schoolYearRepository = schoolYearRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Create a new SchoolYear
     * @param request Data Transfer Object containing details of the SchoolYear to be created
     * @return The created SchoolYear entity
     */
    public SchoolYear createSchoolYear(SchoolYearRequestDTO request) {
        SchoolYear schoolYear = modelMapper.map(request, SchoolYear.class);
        return schoolYearRepository.save(schoolYear);
    }

    /**
     * Get a SchoolYear by its ID
     * @param id The ID of the SchoolYear
     * @return The SchoolYear entity
     */
    public SchoolYear getSchoolYearById(Long id) {
        return schoolYearRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("School year not found"));
    }
}
