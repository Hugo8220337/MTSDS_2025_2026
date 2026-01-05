package com.academins.academins.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing the association between Student, AcademyClass, CurricularUnit, and SchoolYear.
 */
@Entity
@Getter
@Table(name = "student_class_cu")
public class StudentClassCU {

    /**
     * Composite primary key for StudentClassCU entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many-to-one relationship to SchoolYear entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_year_id", nullable = false)
    private SchoolYear schoolYear;


    /**
     * Many-to-one relationship to Student entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    /**
     * Many-to-one relationship to AcademyClass entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academy_class_id", nullable = false)
    private AcademyClass academyClass;

    /**
     * Many-to-one relationship to CurricularUnit entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curricular_unit_id", nullable = false)
    private CurricularUnit curricularUnit;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    protected StudentClassCU() {} // JPA requirement

    public StudentClassCU(SchoolYear schoolYear, Student student, AcademyClass academyClass, CurricularUnit curricularUnit) {
        this.schoolYear = schoolYear;
        this.student = student;
        this.academyClass = academyClass;
        this.curricularUnit = curricularUnit;
    }
}
