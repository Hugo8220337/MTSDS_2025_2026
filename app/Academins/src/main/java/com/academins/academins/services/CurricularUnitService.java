package com.academins.academins.services;

import com.academins.academins.dto.request.CurricularUnitRequestDTO;
import com.academins.academins.dto.response.CurricularUnitResponseDTO;
import com.academins.academins.entities.CurricularUnit;
import com.academins.academins.repositories.CurricularUnitsRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Curricular Units.
 * Provides methods to create, retrieve, and manage Curricular Units.
 */
@Service
public class CurricularUnitService {

    private final CurricularUnitsRepository curricularUnitsRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CurricularUnitService(CurricularUnitsRepository curricularUnitsRepository, ModelMapper modelMapper) {
        this.curricularUnitsRepository = curricularUnitsRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Get all Curricular Units
     *
     * @return List of Curricular Unit DTOs
     */
    public List<CurricularUnitResponseDTO> getAllCurricularUnits() {
        return curricularUnitsRepository.findAll().stream()
                .map(cu -> modelMapper.map(cu, CurricularUnitResponseDTO.class))
                .toList();
    }

    /**
     * Get a Curricular Unit by its ID
     *
     * @param id The ID of the Curricular Unit
     * @return The Curricular Unit DTO if found, otherwise null
     */
    public CurricularUnit getCurricularUnitById(Long id) {
        return curricularUnitsRepository.findById(id).orElseThrow(
                () -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Curricular Unit not found"
                )
        );
    }


    /**
     * Create a new Curricular Unit
     *
     * @param request Data Transfer Object containing details of the Curricular Unit to be created
     * @return The created Curricular Unit
     * @throws IllegalArgumentException if a Curricular Unit with the same code or name already exists
     */
    @Transactional
    public CurricularUnit createCurricularUnit(CurricularUnitRequestDTO request) {
        if (curricularUnitsRepository.existsByCodeCU(request.getCodeCU()) ||
                curricularUnitsRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Curricular Unit with the same code or name already exists.");
        }
        var curricularUnit = modelMapper.map(request, CurricularUnit.class);
        return curricularUnitsRepository.save(curricularUnit);
    }

    /**
     * Update an existing Curricular Unit
     *
     * @param id      The ID of the Curricular Unit to be updated
     * @param request Data Transfer Object containing updated details of the Curricular Unit
     * @return The updated Curricular Unit DTO
     * @throws IllegalArgumentException if the Curricular Unit does not exist
     */
    public CurricularUnit updateCurricularUnit(Long id, CurricularUnitRequestDTO request) {
        var existingCU = curricularUnitsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curricular Unit not found"));

        // Update fields Keeping the ID unchanged
        existingCU.setName(request.getName());
        existingCU.setCodeCU(request.getCodeCU());
        existingCU.setCredits(request.getCredits());

        return curricularUnitsRepository.save(existingCU);
    }

    /**
     * Assign classes to a Curricular Unit
     *
     * @param curricularUnitId The ID of the Curricular Unit
     * @param classIds         List of class IDs to be assigned to the Curricular Unit
     */
    public void assignClassesToCurricularUnit(Long curricularUnitId, List<Long> classIds) {
        var curricularUnit = curricularUnitsRepository.findById(curricularUnitId)
                .orElseThrow(() -> new IllegalArgumentException("Curricular Unit not found"));

        //TODO: Temporary implementation, replace with actual Class entities retrieval

        curricularUnitsRepository.save(curricularUnit);
    }


}
