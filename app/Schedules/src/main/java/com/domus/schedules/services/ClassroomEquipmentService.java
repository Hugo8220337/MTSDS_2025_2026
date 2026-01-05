package com.domus.schedules.services;

import com.domus.schedules.dto.request.ClassroomEquipmentRequestDTO;
import com.domus.schedules.dto.response.ClassroomEquipmentResponseDTO;
import com.domus.schedules.entities.Classroom;
import com.domus.schedules.entities.ClassroomEquipment;
import com.domus.schedules.entities.Equipment;
import com.domus.schedules.repositories.ClassroomEquipmentRepository;
import com.domus.schedules.repositories.ClassroomRepository;
import com.domus.schedules.repositories.EquipmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassroomEquipmentService {
    private final ModelMapper mapper;
    private final ClassroomEquipmentRepository classroomEquipmentRepository;
    private final ClassroomRepository classroomRepository;
    private final EquipmentRepository equipmentRepository;

    public ClassroomEquipmentService(ModelMapper mapper, ClassroomEquipmentRepository classroomEquipmentRepository, ClassroomRepository classroomRepository, EquipmentRepository equipmentRepository) {
        this.mapper = mapper;
        this.classroomEquipmentRepository = classroomEquipmentRepository;
        this.classroomRepository = classroomRepository;
        this.equipmentRepository = equipmentRepository;
    }

    public ClassroomEquipment addEquipmentToClassroom(Long classroomId, ClassroomEquipmentRequestDTO request) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("Classroom not found with id: " + classroomId));

        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found with id: " + request.getEquipmentId()));

        ClassroomEquipment classroomEquipment = new ClassroomEquipment(
                classroom,
                equipment,
                request.getQuantity(),
                request.getObservations()
        );

        return classroomEquipmentRepository.save(classroomEquipment);
    }

    public void removeEquipmentFromClassroom(Long classroomId, Long equipmentId) {
        ClassroomEquipment classroomEquipment = classroomEquipmentRepository.findByClassroomIdAndEquipmentId(classroomId, equipmentId);
        if (classroomEquipment == null) {
            throw new IllegalArgumentException("No equipment found with id: " + equipmentId + " in classroom with id: " + classroomId);
        }
        classroomEquipmentRepository.delete(classroomEquipment);
    }
}
