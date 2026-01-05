package com.academins.academins.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an academic class in the system.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class AcademyClass {

    /**
     * Primary key for the AcademyClass entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    /**
     * Foreign key referencing the CurricularUnit entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_year_id", nullable = false)
    private SchoolYear schoolYear;

    /**
     * Class code combining course acronym, year, and section.
     */
    @Column(nullable = false, length = 20)
    private String classCode; // e.g., "LEI1A", "MEI2A" (Course acronym + year + section)

    /**
     * Many-to-one relationship to CurricularUnit entity.
     */
    @Column(nullable = false)
    private int maxVacancies;

    /**
     * One-to-many relationship to Student entity.
     */
    @OneToMany(fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
