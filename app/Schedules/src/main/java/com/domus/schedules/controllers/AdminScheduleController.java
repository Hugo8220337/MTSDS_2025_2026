package com.domus.schedules.controllers;

import com.domus.schedules.dto.request.ClassGroupScheduleRequestDTO;
import com.domus.schedules.dto.response.ClassGroupScheduleResponseDTO;
import com.domus.schedules.dto.response.LessonResponseDTO;
import com.domus.schedules.entities.ClassGroupSchedule;
import com.domus.schedules.services.ClassGroupScheduleService;
import com.domus.schedules.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for administrative class schedule management.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/schedules/admin")
public class AdminScheduleController {

    private final ModelMapper mapper;
    private final ClassGroupScheduleService classGroupScheduleSrvice;

    public AdminScheduleController(ModelMapper mapper, ClassGroupScheduleService classGroupScheduleSrvice) {
        this.mapper = mapper;
        this.classGroupScheduleSrvice = classGroupScheduleSrvice;
    }

    /**
     * Create a class schedule for a semester.
     *
     * @param request the class schedule creation request
     * @return the created class schedule
     */
    @PostMapping("/create-class-schedule")
    public ResponseEntity<ClassGroupScheduleResponseDTO> createClassSchedule(@RequestBody ClassGroupScheduleRequestDTO request) {
        log.info("Creating class schedule: {}", request);
        ClassGroupSchedule newClassGroupSchedule = classGroupScheduleSrvice.createClassGroupSchedule(request);
        ClassGroupScheduleResponseDTO responseDto = mapper.map(newClassGroupSchedule, ClassGroupScheduleResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * List all lessons for a class schedule.
     *
     * @param scheduleId the class schedule ID
     * @return list of lessons
     */
    @GetMapping("/class-schedules/{scheduleId}/lessons")
    public ResponseEntity<List<LessonResponseDTO>> getScheduleLessons(@PathVariable Long scheduleId) {
        log.info("Getting schedule lessons for class: {}", scheduleId);
        List<LessonResponseDTO> lessons = classGroupScheduleSrvice.getLessonsByScheduleId(scheduleId).stream()
                .map(lesson -> mapper.map(lesson, LessonResponseDTO.class))
                .toList();
        return ResponseEntity.ok(lessons);
    }
}

