package com.domus.schedules.controllers;

import com.domus.schedules.dto.request.LessonRequestDTO;
import com.domus.schedules.dto.request.ScheduleExceptionRequestDTO;
import com.domus.schedules.dto.response.LessonResponseDTO;
import com.domus.schedules.dto.response.ScheduleExceptionResponseDTO;
import com.domus.schedules.entities.Lesson;
import com.domus.schedules.entities.ScheduleException;
import com.domus.schedules.services.ScheduleExceptionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for administrative lesson and exception management.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/schedules/admin")
public class AdminLessonController {
    private final ModelMapper mapper;
    private final ScheduleExceptionService scheduleExceptionService;

    public AdminLessonController(ModelMapper mapper, ScheduleExceptionService scheduleExceptionService) {
        this.mapper = mapper;
        this.scheduleExceptionService = scheduleExceptionService;
    }

    /**
     * Create a periodic lesson (repeats weekly).
     *
     * @param scheduleId the class schedule ID
     * @param request    the lesson creation request
     * @return the created lesson
     */
    @PostMapping("/class-schedules/{scheduleId}/create-periodic-lesson")
    public ResponseEntity<LessonResponseDTO> createPeriodicLesson(
            @PathVariable Long scheduleId,
            @RequestBody LessonRequestDTO request) {
        log.info("createPeriodicLesson called for scheduleId: {}", scheduleId);
        Lesson newLesson = scheduleExceptionService.createPeriodicLesson(scheduleId, request);
        LessonResponseDTO dto = mapper.map(newLesson, LessonResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * List all exceptions for a lesson.
     *
     * @param lessonId the lesson ID
     * @return list of exceptions
     */
    @GetMapping("/lessons/{lessonId}/exceptions")
    public ResponseEntity<List<ScheduleExceptionResponseDTO>> getLessonExceptions(@PathVariable Long lessonId) {
        log.info("getLessonExceptions called for lessonId: {}", lessonId);
        List<ScheduleExceptionResponseDTO> exceptions = scheduleExceptionService.getExceptionsByLessonId(lessonId).stream()
                .map(ex -> mapper.map(ex, ScheduleExceptionResponseDTO.class))
                .toList();
        return ResponseEntity.ok(exceptions);
    }

    /**
     * Create an exception for a lesson (cancellation, room change, etc.).
     *
     * @param lessonId the lesson ID
     * @param request  the exception creation request
     * @return the created exception
     */
    @PostMapping("/lessons/{lessonId}/create-exception")
    public ResponseEntity<ScheduleExceptionResponseDTO> createLessonException(
            @PathVariable Long lessonId,
            @RequestBody ScheduleExceptionRequestDTO request) {
        log.info("createLessonException called for lessonId: {}", lessonId);
        request.setLessonId(lessonId);
        ScheduleException newScheduleException = scheduleExceptionService.createException(request);
        ScheduleExceptionResponseDTO dto = mapper.map(newScheduleException, ScheduleExceptionResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Update an exception for a lesson.
     *
     * @param scheduleExceptionId the schedule exception ID
     * @param request             the exception update request
     * @return the updated exception
     */
    @PutMapping("/exception/{scheduleExceptionId}/update")
    public ResponseEntity<ScheduleExceptionResponseDTO> updateLessonException(
                                                    @PathVariable Long scheduleExceptionId,
                                                    @RequestBody ScheduleExceptionRequestDTO request) {
        log.info("updateLessonException called for scheduleExceptionId: {}", scheduleExceptionId);
        ScheduleException updatedScheduleException = scheduleExceptionService.updateException(scheduleExceptionId, request);
        ScheduleExceptionResponseDTO dto = mapper.map(updatedScheduleException, ScheduleExceptionResponseDTO.class);
        return ResponseEntity.ok(dto);
    }
}

