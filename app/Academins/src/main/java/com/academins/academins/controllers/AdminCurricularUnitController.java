package com.academins.academins.controllers;

import com.academins.academins.dto.request.CurricularUnitRequestDTO;
import com.academins.academins.dto.response.CurricularUnitResponseDTO;
import com.academins.academins.entities.CurricularUnit;
import com.academins.academins.services.CurricularUnitService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for managing Curricular Units by Admins.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/academins/admin/curricular-units")
public class AdminCurricularUnitController {

    private final ModelMapper mapper;
    private final CurricularUnitService curricularUnitService;

    public AdminCurricularUnitController(ModelMapper mapper, CurricularUnitService curricularUnitService) {
        this.mapper = mapper;
        this.curricularUnitService = curricularUnitService;
    }

    /**
     * Get all Curricular Units
     *
     * @return ResponseEntity containing the list of Curricular Unit DTOs and HTTP status
     */
    @GetMapping
    public ResponseEntity<List<CurricularUnitResponseDTO>> getAllCurricularUnits() {
        log.info("getAllCurricularUnits");

        List<CurricularUnitResponseDTO> curricularUnits = curricularUnitService.getAllCurricularUnits();
        return ResponseEntity.ok(curricularUnits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurricularUnitResponseDTO> getCurricularUnitById(@PathVariable Long id) {
        log.info("getCurricularUnitById: {}", id);
        CurricularUnit curricularUnit = curricularUnitService.getCurricularUnitById(id);
        CurricularUnitResponseDTO responseDTO = mapper.map(curricularUnit, CurricularUnitResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Create a new Curricular Unit
     *
     * @param request Data Transfer Object containing details of the Curricular Unit to be created
     * @return ResponseEntity containing the created Curricular Unit DTO and HTTP status
     */
    @PostMapping("/create-curricular-unit")
    public ResponseEntity<CurricularUnitResponseDTO> createCurricularUnit(@RequestBody CurricularUnitRequestDTO request) {
        log.info("createCurricularUnit: {}", request);

        CurricularUnit createdCU = curricularUnitService.createCurricularUnit(request);
        CurricularUnitResponseDTO responseDTO = mapper.map(createdCU, CurricularUnitResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Update an existing Curricular Unit
     *
     * @param id      The ID of the Curricular Unit to be updated
     * @param request Data Transfer Object containing updated details of the Curricular Unit
     * @return ResponseEntity containing the updated Curricular Unit DTO and HTTP status
     */
    @PutMapping("/{id}")
    public ResponseEntity<CurricularUnitResponseDTO> updateCurricularUnit(@PathVariable Long id, @RequestBody CurricularUnitRequestDTO request) {
        log.info("Updating a Curricular Unit: {}", request);

        CurricularUnit updatedCU = curricularUnitService.updateCurricularUnit(id, request);
        CurricularUnitResponseDTO responseDTO = mapper.map(updatedCU, CurricularUnitResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Assign classes to a Curricular Unit
     *
     * @param id       The ID of the Curricular Unit
     * @param classIds List of class IDs to be assigned to the Curricular Unit
     * @return ResponseEntity with HTTP status
     */
    @PostMapping("/{id}/classes")
    public ResponseEntity<?> assignClassesToCurricularUnit(@PathVariable Long id, @RequestBody List<Long> classIds) {
        // TODO ainda não está feito
        log.info("Assigning classes to Curricular Unit: {}", classIds);

        curricularUnitService.assignClassesToCurricularUnit(id, classIds);
        return ResponseEntity.ok(Map.of("message", "Classes assigned successfully"));
    }

}
