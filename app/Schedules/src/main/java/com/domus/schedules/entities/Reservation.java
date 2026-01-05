package com.domus.schedules.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Reservation aggregate root representing point-in-time classroom bookings.
 * Used for tests, meetings, events, and other non-recurring activities.
 */
@Entity
@Getter
@Setter
@Table(name = "reservation", indexes = {
        @Index(name = "idx_reservation_classroom", columnList = "classroom_id, date, startTime, endTime"),
})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(name = "responsible_id")
    private Long responsibleId;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private long version;


    protected Reservation() {
    }

    public Reservation(Classroom classroom, String title,
                       LocalDate date, LocalTime startTime, LocalTime endTime,
                       Long responsibleId, String observations) {
        this.classroom = classroom;
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.responsibleId = responsibleId;
        this.observations = observations;
    }


    /**
     * Relocates the reservation to a different classroom.
     *
     * @param newClassroom the new classroom
     */
    public void relocate(Classroom newClassroom) {
        this.classroom = newClassroom;
    }

    /**
     * Updates the reservation title.
     *
     * @param newTitle the new title
     */
    public void updateTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     * Reschedules the reservation to a new time period.
     *
     * @param newDate new  date
     * @param newStartTime new start time
     * @param newEndTime   new end time
     */
    public void reschedule(LocalDate newDate, LocalTime newStartTime, LocalTime newEndTime) {
        this.date = newDate;
        this.startTime = newStartTime;
        this.endTime = newEndTime;
    }

    /**
     * Gets the start datetime of the reservation.
     *
     * @return start datetime
     */
    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(date, startTime);
    }

    /**
     * Gets the end datetime of the reservation.
     *
     * @return end datetime
     */
    public LocalDateTime getEndDateTime() {
        return LocalDateTime.of(date, endTime);
    }

    /**
     * Changes the responsible person.
     *
     * @param newResponsibleId the new responsible person ID
     */
    public void changeResponsible(Long newResponsibleId) {
        this.responsibleId = newResponsibleId;
    }

    /**
     * Adds observations to the reservation.
     *
     * @param additionalObservations notes to add
     */
    public void addObservations(String additionalObservations) {
        if (additionalObservations == null || additionalObservations.isBlank()) {
            return;
        }
        if (this.observations == null || this.observations.isBlank()) {
            this.observations = additionalObservations;
        } else {
            this.observations += "\n" + additionalObservations;
        }
    }

    /**
     * Gets the start datetime of the reservation.
     *
     * @param other the other reservation
     * @return true if there is a conflict
     */
    public boolean conflictsWith(Reservation other) {
        if (other == null || !this.classroom.equals(other.classroom)) {
            return false;
        }
        LocalDateTime aStart = this.getStartDateTime();
        LocalDateTime aEnd = this.getEndDateTime();
        LocalDateTime bStart = other.getStartDateTime();
        LocalDateTime bEnd = other.getEndDateTime();
        return aStart.isBefore(bEnd) && aEnd.isAfter(bStart);
    }

    /**
     * Gets the start datetime of the reservation.
     *
     * @return start datetime
     */
    public boolean isPast() {
        return getEndDateTime().isBefore(LocalDateTime.now());
    }

    /**
     * Checks if this reservation is in the future.
     *
     * @return true if the reservation is upcoming
     */
    public boolean isFuture() {
        return getStartDateTime().isAfter(LocalDateTime.now());
    }


    /**
     * Checks if this reservation is active at a specific time.
     *
     * @param datetime the time to check
     * @return true if the reservation is active
     */
    public boolean isActiveAt(LocalDateTime datetime) {
        return !datetime.isBefore(getStartDateTime()) && datetime.isBefore(getEndDateTime());
    }

    /**
     * Checks if this reservation is currently active.
     *
     * @return true if the reservation is happening now
     */
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(getStartDateTime()) && now.isBefore(getEndDateTime());
    }

    /**
     * Gets the duration of the reservation in minutes.
     *
     * @return duration in minutes
     */
    public long getDurationInMinutes() {
        return Duration.between(getStartDateTime(), getEndDateTime()).toMinutes();
    }

    /**
     * Checks if this reservation belongs to a specific person.
     *
     * @param personId the person ID
     * @return true if matches
     */
    public boolean isManagedBy(Long personId) {
        return this.responsibleId != null && this.responsibleId.equals(personId);
    }
}

