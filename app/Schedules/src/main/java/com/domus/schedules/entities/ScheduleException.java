package com.domus.schedules.entities;

import com.domus.schedules.valueObjects.ExceptionType;
import com.domus.schedules.valueObjects.TimeSlot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * ScheduleException entity representing specific exceptions to recurring lessons.
 * Part of the ClassGroupSchedule aggregate.
 */
@Entity
@Getter
@Setter
@Table(name = "schedule_exception", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"lesson_id", "date"})
}, indexes = {
    @Index(name = "idx_exception_date", columnList = "date")
})
public class ScheduleException {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ExceptionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alternative_classroom_id")
    private Classroom alternativeClassroom;

    @Column(name = "alternative_start_time")
    private LocalTime alternativeStartTime;

    @Column(name = "alternative_end_time")
    private LocalTime alternativeEndTime;

    @Column(name = "substitute_instructor_id")
    private Long substituteInstructorId;

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

    protected ScheduleException() {}

    /**
     * Creates a new schedule exception.
     * @param lesson the lesson being modified
     * @param date the specific date of the exception
     * @param type the type of exception
     */
    public ScheduleException(Lesson lesson, LocalDate date, ExceptionType type) {
        this.lesson = lesson;
        this.date = date;
        this.type = type;
    }

    /**
     * Changes the classroom for this exception.
     * Only valid for ROOM_CHANGE exceptions.
     * @param newClassroom the alternative classroom
     */
    public void changeClassroom(Classroom newClassroom) {
        if (this.type != ExceptionType.ROOM_CHANGE) {
            throw new IllegalStateException("Can only change classroom for ROOM_CHANGE exceptions");
        }
        if (newClassroom == null) {
            throw new IllegalArgumentException("Alternative classroom cannot be null");
        }
        this.alternativeClassroom = newClassroom;
    }

    /**
     * Changes the time for this exception.
     * Only valid for TIME_CHANGE exceptions.
     * @param newTimeSlot the alternative time slot
     */
    public void changeTime(TimeSlot newTimeSlot) {
        if (this.type != ExceptionType.TIME_CHANGE) {
            throw new IllegalStateException("Can only change time for TIME_CHANGE exceptions");
        }
        if (newTimeSlot == null) {
            throw new IllegalArgumentException("Alternative time slot cannot be null");
        }
        this.alternativeStartTime = newTimeSlot.getStartTime();
        this.alternativeEndTime = newTimeSlot.getEndTime();
    }

    /**
     * Changes the instructor for this exception.
     * Only valid for INSTRUCTOR_CHANGE exceptions.
     * @param newInstructorId the substitute instructor ID
     */
    public void changeInstructor(Long newInstructorId) {
        if (this.type != ExceptionType.INSTRUCTOR_CHANGE) {
            throw new IllegalStateException("Can only change instructor for INSTRUCTOR_CHANGE exceptions");
        }
        if (newInstructorId == null || newInstructorId <= 0) {
            throw new IllegalArgumentException("Invalid instructor ID");
        }
        this.substituteInstructorId = newInstructorId;
    }

    /**
     * Adds observations about this exception.
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
     * Checks if this exception applies to a given date.
     * @param date the date to check
     * @return true if this exception applies
     */
    public boolean appliesTo(LocalDate date) {
        return this.date.equals(date);
    }

    /**
     * Checks if this is a cancellation.
     * @return true if the lesson is cancelled
     */
    public boolean isCancellation() {
        return this.type == ExceptionType.CANCELLATION || this.type == ExceptionType.HOLIDAY;
    }

    /**
     * Checks if this exception requires alternative arrangements.
     * @return true if alternative classroom, time, or instructor is needed
     */
    public boolean requiresAlternative() {
        return this.type == ExceptionType.ROOM_CHANGE ||
               this.type == ExceptionType.TIME_CHANGE ||
               this.type == ExceptionType.INSTRUCTOR_CHANGE;
    }

}

