package com.domus.schedules.controllers;

import com.domus.schedules.dto.response.EquipmentResponseDTO;
import com.domus.schedules.services.EquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for administrative equipment management.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/schedules/admin/equipment")
public class AdminEquipmentController {

    private final EquipmentService equipmentService;

    public AdminEquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    /**
     * List all available equipment types.
     *
     * @return list of equipment
     */
    @GetMapping
    public ResponseEntity<List<EquipmentResponseDTO>> getAllEquipment() {
        log.info("getAllEquipment called");
        List<EquipmentResponseDTO> equipments = equipmentService.getAllEquipments().stream()
                .map(equipment -> EquipmentResponseDTO.builder()
                        .id(equipment.getId())
                        .name(equipment.getName())
                        .description(equipment.getDescription())
                        .build())
                .toList();
        return ResponseEntity.ok(equipments);
    }
}

