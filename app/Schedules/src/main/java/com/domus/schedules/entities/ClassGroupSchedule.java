package com.domus.schedules.entities;

import com.domus.schedules.valueObjects.AcademicPeriod;
import com.domus.schedules.valueObjects.EntityId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ClassGroupSchedule aggregate root representing the schedule context for a class group
 * in a specific course unit during an academic period.
 * Manages lessons and their temporal boundaries.
 *
 * Nome Português: HorarioTurma
 */
@Entity
@Getter
@Setter
@Table(name = "class_group_schedule", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"class_group_id", "course_unit_id", "academic_year_id"})
})
public class ClassGroupSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_group_id", nullable = false)
    private Long classGroupId;

    @Column(name = "course_unit_id", nullable = false)
    private Long courseUnitId;

    @Column(name = "academic_year_id", nullable = false)
    private Long academicYearId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "semester", column = @Column(name = "semester", nullable = false)),
        @AttributeOverride(name = "startDate", column = @Column(name = "start_date", nullable = false)),
        @AttributeOverride(name = "endDate", column = @Column(name = "end_date", nullable = false))
    })
    private AcademicPeriod academicPeriod;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    protected ClassGroupSchedule() {}

    /**
     * Creates a new ClassGroupSchedule for a class group in a course unit.
     * @param classGroupId FK to class group in Academic Management service
     * @param courseUnitId FK to course unit in Academic Management service
     * @param academicYearId FK to academic year in Academic Management service
     * @param academicPeriod the academic period (semester, dates)
     */
    public ClassGroupSchedule(Long classGroupId, Long courseUnitId, Long academicYearId,
                             AcademicPeriod academicPeriod) {
        this.classGroupId = classGroupId;
        this.courseUnitId = courseUnitId;
        this.academicYearId = academicYearId;
        this.academicPeriod = academicPeriod;
    }

    /**
     * Extends the schedule period.
     * @param newEndDate the new end date
     * @throws IllegalArgumentException if the new date is invalid
     */
    public void extendPeriod(LocalDate newEndDate) {
        if (newEndDate == null || !newEndDate.isAfter(academicPeriod.getEndDate())) {
            throw new IllegalArgumentException("New end date must be after current end date");
        }
        this.academicPeriod = new AcademicPeriod(
            academicPeriod.getStartDate(),
            newEndDate,
            academicPeriod.getSemester()
        );
    }

    /**
     * Checks if the schedule is active on a given date.
     * @param date the date to check
     * @return true if the schedule is active
     */
    public boolean isActiveOn(LocalDate date) {
        return academicPeriod.isActive(date);
    }

    /**
     * Checks if this schedule belongs to a specific class group.
     * @param classGroupId the class group ID
     * @return true if matches
     */
    public boolean belongsToClassGroup(Long classGroupId) {
        return this.classGroupId.equals(classGroupId);
    }

    /**
     * Checks if this schedule is for a specific course unit.
     * @param courseUnitId the course unit ID
     * @return true if matches
     */
    public boolean isForCourseUnit(Long courseUnitId) {
        return this.courseUnitId.equals(courseUnitId);
    }

    /**
     * Checks if this schedule is in a specific academic year.
     * @param academicYearId the academic year ID
     * @return true if matches
     */
    public boolean isInAcademicYear(Long academicYearId) {
        return this.academicYearId.equals(academicYearId);
    }
}

