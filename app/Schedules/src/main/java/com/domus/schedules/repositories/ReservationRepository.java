package com.domus.schedules.repositories;

import com.domus.schedules.entities.Reservation;
import com.domus.schedules.entities.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Reservation aggregate root.
 * Manages point-in-time classroom bookings.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.classroom.id = :classroomId " +
            "AND ((r.date >= :startDate AND r.date <= :endDate))")
    List<Reservation> getReservationsByClassroomIdAndDateTimeRange(
            @Param("classroomId") Long classroomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT r FROM Reservation r WHERE r.responsibleId = :responsibleId")
    List<Reservation> findByResponsibleId(Long responsibleId);

    @Query("SELECT r FROM Reservation r WHERE r.responsibleId = :responsibleId " +
            "AND ((r.date >= :startDate AND r.date <= :endDate))")
    List<Reservation> findByResponsibleIdAndDateRange(Long responsibleId, LocalDate startDate, LocalDate endDate);

    @Query("""
                SELECT r
                FROM Reservation r
                JOIN r.classroom c
                WHERE c.schoolId = :schoolId
                  AND r.date = :date
            """)
    List<Reservation> findBySchoolIdAndDate(Long schoolId, LocalDate date);
}

