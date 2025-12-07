package com.domus.enrollments.services;

import com.domus.enrollments.dto.request.ChangeEnrollmentStateRequestDto;
import com.domus.enrollments.dto.request.EnrollmentRequestDTO;
import com.domus.enrollments.entities.Enrollment;
import com.domus.enrollments.repositories.EnrollmentRepository;
import com.domus.enrollments.valueObjects.EnrollmentState;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private final ModelMapper mapper;
    private final EnrollmentRepository enrollmentRepository;


    @Autowired
    public EnrollmentService(ModelMapper mapper, EnrollmentRepository enrollmentRepository) {
        this.mapper = mapper;
        this.enrollmentRepository = enrollmentRepository;

    }

    /**
     * Create a new Enrollment
     * @param request Data Transfer Object containing details of the Enrollment to be created
     * @return The created Enrollment entity
     */
    public Enrollment createEnrollment(EnrollmentRequestDTO request) {
        if(request == null) {
            throw new IllegalArgumentException("Enrollment data cannot be null");
        }
        Enrollment enrollment = mapper.map(request, Enrollment.class);
        return enrollmentRepository.save(enrollment);
    }

    /**
     * Get an Enrollment by its ID
     * @param id The ID of the Enrollment
     * @return The Enrollment entity if found, otherwise null
     */
    public Enrollment findEnrollmentById(Long id) {
        if(!enrollmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Enrollment with ID " + id + " does not exist.");
        }
        return enrollmentRepository.findById(id).orElse(null);
    }

    /**
     * Get all Enrollments
     * @return List of all Enrollment entities
     */
    public List<Enrollment> findAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    /**
     * Update the state of an existing Enrollment
     * @param id The ID of the Enrollment to be updated
     * @param request The new state to set
     * @return The updated Enrollment entity
     * @throws IllegalArgumentException if the Enrollment with the given ID does not exist
     */
    public Enrollment updateEnrollmentState(Long id, ChangeEnrollmentStateRequestDto request) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment with ID " + id + " does not exist."));

        try {
            enrollment.setEnrollmentState(request.getNewState());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid enrollment state: " + request.getNewState());
        }
        return enrollmentRepository.save(enrollment);
    }

}
