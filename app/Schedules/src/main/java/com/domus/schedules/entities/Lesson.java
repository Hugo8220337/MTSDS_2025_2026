package com.domus.schedules.entities;

import com.domus.schedules.valueObjects.LessonType;
import com.domus.schedules.valueObjects.TimeSlot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * Lesson entity representing a recurring weekly lesson.
 * Part of the ClassGroupSchedule aggregate.
 */
@Entity
@Getter
@Setter
@Table(name = "lesson", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"class_group_schedule_id", "day_of_week", "start_time"})
}, indexes = {
    @Index(name = "idx_lesson_classroom_conflict", columnList = "classroom_id, day_of_week, start_time, end_time"),
    @Index(name = "idx_lesson_instructor_conflict", columnList = "instructor_id, day_of_week, start_time, end_time")
})
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_group_schedule_id", nullable = false)
    private ClassGroupSchedule classGroupSchedule;

    @Column(name = "instructor_id", nullable = false)
    private Long instructorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "startTime", column = @Column(name = "start_time", nullable = false)),
        @AttributeOverride(name = "endTime", column = @Column(name = "end_time", nullable = false))
    })
    private TimeSlot timeSlot;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_type", nullable = false, length = 50)
    private LessonType lessonType;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private long version;


    protected Lesson() {}

    /**
     * Creates a new recurring Lesson.
     * @param classGroupSchedule the schedule context
     * @param instructorId FK to instructor
     * @param classroom the classroom
     * @param dayOfWeek day of the week
     * @param timeSlot time slot
     * @param lessonType type of lesson
     */
    public Lesson(ClassGroupSchedule classGroupSchedule, Long instructorId, Classroom classroom,
                 DayOfWeek dayOfWeek, TimeSlot timeSlot, LessonType lessonType) {
        this.classGroupSchedule = classGroupSchedule;
        this.instructorId = instructorId;
        this.classroom = classroom;
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
        this.lessonType = lessonType;
        this.durationMinutes = timeSlot.getDurationInMinutes();
    }

    /**
     * Changes the instructor for this lesson.
     * @param newInstructorId the new instructor ID
     */
    public void changeInstructor(Long newInstructorId) {
        this.instructorId = newInstructorId;
    }

    /**
     * Relocates the lesson to a different classroom.
     * @param newClassroom the new classroom
     */
    public void relocate(Classroom newClassroom) {
        this.classroom = newClassroom;
    }

    /**
     * Reschedules the lesson to a different day.
     * @param newDayOfWeek the new day
     */
    public void reschedule(DayOfWeek newDayOfWeek) {
        this.dayOfWeek = newDayOfWeek;
    }

    /**
     * Changes the time slot of the lesson.
     * @param newTimeSlot the new time slot
     */
    public void changeTimeSlot(TimeSlot newTimeSlot) {
        this.timeSlot = newTimeSlot;
        this.durationMinutes = newTimeSlot.getDurationInMinutes();
    }

    /**
     * Changes the lesson type.
     * @param newType the new type
     */
    public void changeType(LessonType newType) {
        this.lessonType = newType;
    }

    /**
     * Checks if this lesson conflicts with another lesson's time slot.
     * @param other another lesson
     * @return true if they conflict
     */
    public boolean conflictsWith(Lesson other) {
        if (other == null || !this.dayOfWeek.equals(other.dayOfWeek)) {
            return false;
        }
        return this.timeSlot.overlapsWith(other.timeSlot);
    }

    /**
     * Checks if this lesson is taught by a specific instructor.
     * @param instructorId the instructor ID
     * @return true if matches
     */
    public boolean isTaughtBy(Long instructorId) {
        return this.instructorId.equals(instructorId);
    }

    /**
     * Checks if this lesson takes place in a specific classroom.
     * @param classroom the classroom
     * @return true if matches
     */
    public boolean isInClassroom(Classroom classroom) {
        return this.classroom.equals(classroom);
    }

    /**
     * Checks if this lesson is on a specific day.
     * @param dayOfWeek the day
     * @return true if matches
     */
    public boolean isOnDay(DayOfWeek dayOfWeek) {
        return this.dayOfWeek.equals(dayOfWeek);
    }
}
