package com.domus.enrollments.entities;

import com.domus.enrollments.valueObjects.EnrollmentState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity class representing an Enrollment.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Enrollment {
    /**
     * Primary key for the Enrollment entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    /**
     * Unique identifier for the student (NIF).
     */
    @Column(unique = true, nullable = false)
    private String nif;

    /**
     * Name of the student.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Identifier for the course.
     */
    @Column(nullable = false)
    private Long courseId;

    /**
     * Academic year for the enrollment.
     */
    @Column(nullable = false)
    private String schoolYear;

    /**
     * Identifier for the application associated with the enrollment.
     */
    @Column(nullable = false)
    private Long applicationId;
    /**
     * State of the enrollment (e.g., PENDING, APPROVED, REJECTED).
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnrollmentState enrollmentState = EnrollmentState.PROCESSING;

    /**
     * Timestamp when the enrollment was created.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Timestamp when the enrollment was last updated.
     */
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Enrollment(Long enrollmentId, String nif, String name, Long courseId, String schoolYear, Long applicationId) {
        this.enrollmentId = enrollmentId;
        this.nif = nif;
        this.name = name;
        this.courseId = courseId;
        this.schoolYear = schoolYear;
        this.applicationId = applicationId;
    }
}
