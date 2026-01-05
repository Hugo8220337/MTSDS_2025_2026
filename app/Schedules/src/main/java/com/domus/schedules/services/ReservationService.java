package com.domus.schedules.services;

import com.domus.schedules.client.AcademinsApiClient;
import com.domus.schedules.configs.MQConfig;
import com.domus.schedules.dto.request.ReservationRequestDTO;
import com.domus.schedules.entities.Classroom;
import com.domus.schedules.entities.Reservation;
import com.domus.schedules.exceptions.ResourceNotFoundException;
import com.domus.schedules.messages.SendNotificationMessage;
import com.domus.schedules.repositories.ClassroomRepository;
import com.domus.schedules.repositories.ReservationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final ModelMapper mapper;
    private final AcademinsApiClient academinsApiClient;
    private final ClassroomRepository classroomRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(ModelMapper mapper, AcademinsApiClient academinsApiClient, ClassroomRepository classroomRepository, ReservationRepository reservationRepository) {
        this.mapper = mapper;
        this.academinsApiClient = academinsApiClient;
        this.classroomRepository = classroomRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getReservationsByResponsibleId(Long ResponsibleId, LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findByResponsibleIdAndDateRange(ResponsibleId, startDate, endDate);
    }

    public void deleteReservation(Long reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new ResourceNotFoundException("Reservation not found with id: " + reservationId);
        }

        reservationRepository.deleteById(reservationId);
    }

    public Reservation createReservation(ReservationRequestDTO req) {
        Classroom classroom = classroomRepository.findById(req.getClassroomId()).orElseThrow(
                () -> new ResourceNotFoundException("Classroom does not exist.")
        );

      if (!academinsApiClient.existsTeacher(req.getResponsibleId())) {
          throw new ResourceNotFoundException("Assigned responible teacher does not exist.");
      }

        Reservation reservation = mapper.map(req, Reservation.class);

        // set Relationships
        reservation.setClassroom(classroom);

        return reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));
    }

    public Reservation updateReservation(Long reservationId, ReservationRequestDTO request) {
        // Check if the reservation exists
        Reservation existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

        // Check if the classroom exists
        if (!classroomRepository.existsById(request.getClassroomId())) {
            throw new ResourceNotFoundException("Classroom does not exist.");
        }

        // Check if the responsible teacher exists
        if (!academinsApiClient.existsTeacher(request.getResponsibleId())) {
            throw new ResourceNotFoundException("Assigned responible teacher does not exist.");
        }

        // Update fields Keeping the ID unchanged
        mapper.map(request, existingReservation);

        return reservationRepository.save(existingReservation);
    }
}
