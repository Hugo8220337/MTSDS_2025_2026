package com.academins.academins.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a Study Plan in the academic system.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class StudyPlan {

    /**
     * Primary key for the StudyPlan entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many-to-one relationship with the Course entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * Many-to-one relationship with the School entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    /**
     * Many-to-one relationship with the CurricularUnit entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curricular_unit_id", nullable = false)
    private CurricularUnit curricularUnit;

    /**
     * Curricular year for the study plan.
     */
    @Column(nullable = false)
    private int curricularYear;

    /**
     * Semester for the study plan (1 or 2).
     */
    @Column(name = "semester", nullable = false)
    @Check(constraints = "semester IN (1, 2)")
    private short semester;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
