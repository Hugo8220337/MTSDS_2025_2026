package com.academins.academins.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing the association between Teacher and Curricular Units for a specific School Year.
 */
@Entity
@Table(name = "teacher_cu")
@Setter
@Getter
public class TeacherCU {

    /**
     * Composite primary key for TeacherCU entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many-to-one relationship to Teacher entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    /**
     * Many-to-one relationship to CurricularUnit entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CurricularUnit curricularUnit;

    /**
     * Many-to-one relationship to SchoolYear entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private SchoolYear schoolYear;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    protected TeacherCU() {} // JPA requirement

    public TeacherCU(Teacher teacher, CurricularUnit curricularUnit, SchoolYear schoolYear) {
        this.teacher = teacher;
        this.curricularUnit = curricularUnit;
        this.schoolYear = schoolYear;
    }
}