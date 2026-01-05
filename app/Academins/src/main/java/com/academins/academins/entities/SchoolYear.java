package com.academins.academins.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Entity representing a School Year in the academic system.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class SchoolYear {

    /**
     * Primary key for the SchoolYear entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_year_id", insertable = false, updatable = false)
    private Long schoolYearId;

    /**
     * Academic year in the format "YYYY/YYYY".
     */
    @Column(nullable = false, unique = true, length = 9)
    private String year; //e.g., "2025/2026"

    /**
     * Start date of the school year.
     */
    @Column(nullable = false)
    private Date startDate;

    /**
     * End date of the school year.
     */
    @Column(nullable = false)
    private Date endDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
