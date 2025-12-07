package com.domus.schedules.controllers;

import com.domus.schedules.dto.request.EquipmentRequestDTO;
import com.domus.schedules.dto.response.EquipmentResponseDTO;
import com.domus.schedules.entities.Equipment;
import com.domus.schedules.services.EquipmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/schedules/admin/equipments")
public class EquipmentController {
    private final ModelMapper mapper;
    private final EquipmentService equipmentService;

    public EquipmentController(ModelMapper mapper, EquipmentService equipmentService) {
        this.mapper = mapper;
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public ResponseEntity<List<EquipmentResponseDTO>> getAllEquipments() {
        log.info("getAllEquipments called");
        List<EquipmentResponseDTO> equipments = equipmentService.getAllEquipments().stream()
                .map(equipment -> mapper.map(equipment, EquipmentResponseDTO.class))
                .toList();
        return ResponseEntity.ok(equipments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponseDTO> getEquipmentById(@PathVariable Long id) {
        log.info("getEquipmentById called with id: {}", id);
        Equipment equipment = equipmentService.getEquipmentById(id);
        EquipmentResponseDTO dto = mapper.map(equipment, EquipmentResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<EquipmentResponseDTO> addEquipment(
            @RequestBody EquipmentRequestDTO equipment
    ) {
        log.info("addEquipment called with equipment {}", equipment);
        Equipment newEquipment = equipmentService.addEquipment(equipment);
        EquipmentResponseDTO dto = mapper.map(newEquipment, EquipmentResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/update-equipment")
    public ResponseEntity<EquipmentResponseDTO> updateEquipment(
            @PathVariable Long id,
            @RequestBody EquipmentRequestDTO equipment
    ) {
        log.info("updateEquipment called with equipment {}", equipment);
        Equipment updatedEquipment = equipmentService.updateEquipment(id, equipment);
        EquipmentResponseDTO dto = mapper.map(updatedEquipment, EquipmentResponseDTO.class);
        return ResponseEntity.ok(dto);
    }
}
