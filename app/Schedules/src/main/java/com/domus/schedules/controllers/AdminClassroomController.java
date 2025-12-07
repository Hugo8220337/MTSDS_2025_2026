package com.domus.schedules.controllers;

import com.domus.schedules.dto.response.ClassroomAvailabilityResponseDTO;
import com.domus.schedules.dto.request.ClassroomEquipmentRequestDTO;
import com.domus.schedules.dto.request.ClassroomRequestDTO;
import com.domus.schedules.dto.response.ClassroomEquipmentResponseDTO;
import com.domus.schedules.dto.response.ClassroomResponseDTO;
import com.domus.schedules.dto.response.EquipmentResponseDTO;
import com.domus.schedules.entities.Classroom;
import com.domus.schedules.entities.ClassroomEquipment;
import com.domus.schedules.entities.Equipment;
import com.domus.schedules.services.ClassroomEquipmentService;
import com.domus.schedules.services.ClassroomService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for administrative classroom management.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/schedules/admin")
public class AdminClassroomController {

    private final ModelMapper mapper;
    private final ClassroomService classroomService;
    private final ClassroomEquipmentService classroomEquipmentService;

    public AdminClassroomController(ModelMapper mapper, ClassroomService classroomService, ClassroomEquipmentService classroomEquipmentService) {
        this.mapper = mapper;
        this.classroomEquipmentService = classroomEquipmentService;
        this.classroomService = classroomService;
    }

    /**
     * Create a new classroom.
     *
     * @param schoolId the school Id
     * @param request  the classroom creation request
     * @return the created classroom
     */
    @PostMapping("/schools/{schoolId}/create-classroom")
    public ResponseEntity<ClassroomResponseDTO> createClassroom(
            @PathVariable Long schoolId,
            @RequestBody ClassroomRequestDTO request) {
        log.info("Creating classroom in school ID {}: {}", schoolId, request);
        Classroom classroom = classroomService.createClassroom(schoolId, request);
        ClassroomResponseDTO dto = mapper.map(classroom, ClassroomResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Get classroom availability for a specific date.
     *
     * @param classroomId the classroom ID
     * @param startDate   the start date
     * @param endDate     the end date
     * @return availability details
     */
    @GetMapping("/classrooms/{classroomId}/availability")
    public ResponseEntity<ClassroomAvailabilityResponseDTO> getClassroomAvailability(
            @PathVariable Long classroomId,
            @RequestParam LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        log.info("Fetching availability for classroom ID {} from {} to {}", classroomId, startDate, endDate);

        if (startDate == null)
            throw new IllegalArgumentException("Start date is required");

        if (endDate == null)
            endDate = startDate;

        ClassroomAvailabilityResponseDTO availability = classroomService.getClassroomAvailability(classroomId, startDate, endDate);
        return ResponseEntity.ok(availability);
    }

    /**
     * Add equipment to a classroom.
     *
     * @param classroomId the classroom ID
     * @param request     the equipment addition request
     * @return the created classroom equipment
     */
    @PostMapping("/classrooms/{classroomId}/equipment")
    public ResponseEntity<ClassroomEquipmentResponseDTO> addEquipmentToClassroom(
            @PathVariable Long classroomId,
            @RequestBody ClassroomEquipmentRequestDTO request) {
        log.info("Adding equipment to classroom ID {}: {}", classroomId, request);
        ClassroomEquipment created = classroomEquipmentService.addEquipmentToClassroom(classroomId, request);
        ClassroomEquipmentResponseDTO dto = mapper.map(created, ClassroomEquipmentResponseDTO.class);
        return ResponseEntity.ok(dto);
    }


    @DeleteMapping("/classrooms/{classroomId}/equipments/{equipmentId}")
    public ResponseEntity<Void> removeEquipmentFromClassroom(
            @PathVariable Long classroomId,
            @PathVariable Long equipmentId
    ) {
        log.info("Removing equipment ID {} from classroom ID {}", equipmentId, classroomId);
        classroomEquipmentService.removeEquipmentFromClassroom(classroomId, equipmentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * List all equipment in a classroom.
     *
     * @param classroomId the classroom ID
     * @return list of equipment
     */
    @GetMapping("/classrooms/{classroomId}/equipment")
    public ResponseEntity<List<EquipmentResponseDTO>> getClassroomEquipment(
            @PathVariable Long classroomId) {
        log.info("Fetching equipment for classroom ID {}", classroomId);
        List<EquipmentResponseDTO> equipments = classroomService.getClassroomEquipment(classroomId).stream()
                .map(equipment -> mapper.map(equipment, EquipmentResponseDTO.class))
                .toList();
        return ResponseEntity.ok(equipments);
    }
}
