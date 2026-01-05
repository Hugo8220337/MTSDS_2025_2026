package com.domus.schedules.controllers;

import com.domus.schedules.dto.response.ReservationResponseDTO;
import com.domus.schedules.entities.Reservation;
import com.domus.schedules.services.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controller for teacher schedule and reservation queries.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/schedules/teachers/me")
public class TeacherScheduleController {

    private final ReservationService reservationService;
    private final ModelMapper mapper;

    public TeacherScheduleController(ReservationService reservationService, ModelMapper mapper) {
        this.mapper = mapper;
        this.reservationService = reservationService;
    }

    /**
     * List teacher's reservations within a date range.
     *
     * @param startDate start date (format: yyyy-MM-dd)
     * @param endDate   end date (format: yyyy-MM-dd)
     * @return list of reservations
     */
    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponseDTO>> getMyReservations(
            @RequestParam Long teacherId, // TODO remover depois que implementar autenticação
            @RequestParam String startDate,
            @RequestParam(required = false) String endDate) {
        log.info("Getting reservations for teacher ID {} from {} to {}", teacherId, startDate, endDate);
        if (startDate == null)
            throw new IllegalArgumentException("startDate must not be null");

        if (endDate == null)
            endDate = startDate;

        LocalDate locaDateStartDate = LocalDate.parse(startDate);
        LocalDate locaDateEndDate = LocalDate.parse(endDate);

        List<Reservation> reservations = reservationService.getReservationsByResponsibleId(teacherId, locaDateStartDate, locaDateEndDate);
        List<ReservationResponseDTO> dtos = reservations.stream()
                .map(r -> mapper.map(r, ReservationResponseDTO.class))
                .toList();

        return ResponseEntity.ok(dtos);
    }
}

