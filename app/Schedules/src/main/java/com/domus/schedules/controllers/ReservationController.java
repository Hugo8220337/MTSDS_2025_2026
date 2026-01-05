package com.domus.schedules.controllers;

import com.domus.schedules.dto.request.ReservationRequestDTO;
import com.domus.schedules.dto.response.ReservationResponseDTO;
import com.domus.schedules.entities.Reservation;
import com.domus.schedules.services.ReservationService;
import com.domus.schedules.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for classroom reservation management.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/schedules")
public class ReservationController {

    private final ModelMapper mapper;
    private final ReservationService reservationService;

    public ReservationController(ModelMapper mapper, ReservationService reservationService) {
        this.mapper = mapper;
        this.reservationService = reservationService;
    }

    /**
     * Create a new classroom reservation.
     *
     * @param request the reservation creation request
     * @return the created reservation
     */
    @PostMapping("/reservate-classroom")
    public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody ReservationRequestDTO request) {
        log.info("Creating reservation: {}", request);
        Reservation newReservation = reservationService.createReservation(request);
        ReservationResponseDTO responseDto = mapper.map(newReservation, ReservationResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Get details of a specific reservation.
     *
     * @param reservationId the reservation ID
     * @return reservation details
     */
    @GetMapping("/reservations/{reservationId}")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable Long reservationId) {
        log.info("Getting reservation: {}", reservationId);
        Reservation reservation = reservationService.getReservationById(reservationId);
        ReservationResponseDTO responseDto = mapper.map(reservation, ReservationResponseDTO.class);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Update an existing reservation.
     *
     * @param reservationId the reservation ID
     * @param request       the reservation update request
     * @return the updated reservation
     */
    @PutMapping("/reservations/{reservationId}")
    public ResponseEntity<ReservationResponseDTO> updateReservation(
            @PathVariable Long reservationId,
            @RequestBody ReservationRequestDTO request) {
        log.info("Updating reservation: {}", request);
        Reservation updatedReservation = reservationService.updateReservation(reservationId, request);
        ReservationResponseDTO responseDto = mapper.map(updatedReservation, ReservationResponseDTO.class);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Cancel a reservation.
     *
     * @param reservationId the reservation ID
     * @return no content
     */
    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId) {
        log.info("Canceling reservation: {}", reservationId);
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}

