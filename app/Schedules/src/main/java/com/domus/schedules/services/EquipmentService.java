package com.domus.schedules.services;

import com.domus.schedules.dto.request.EquipmentRequestDTO;
import com.domus.schedules.entities.Equipment;
import com.domus.schedules.repositories.EquipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {
    private final ModelMapper modelMapper;
    private final EquipmentRepository equipmentRepository;

    public EquipmentService(ModelMapper mapper, EquipmentRepository equipmentRepository) {
        this.modelMapper = mapper;
        this.equipmentRepository = equipmentRepository;
    }

    public Equipment addEquipment(EquipmentRequestDTO equipment) {
        Equipment equipmentEntity = modelMapper.map(equipment, Equipment.class);
        return equipmentRepository.save(equipmentEntity);
    }

    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    public Equipment getEquipmentById(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
    }

    public Equipment updateEquipment(Long id, EquipmentRequestDTO equipment) {
        Equipment existingEquipment = getEquipmentById(id);
        modelMapper.map(equipment, existingEquipment);
        return equipmentRepository.save(existingEquipment);
    }

}
