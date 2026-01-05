package com.academins.academins.entities;

import com.academins.academins.valueObjects.Degree;
import com.academins.academins.valueObjects.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a Course in the academic system.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Course {

    /**
     * Primary key for the Course entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    /**
     * Name of the course.
     */
    @Column(nullable = false, length = 255)
    private String name;

    /**
     * Code of the course.
     */
    @Column(nullable = false, length = 50, unique = true)
    private String courseCode;

    /**
     * Degree associated with the course.
     */
    @Column(nullable = false, length = 50)
    private Degree degree; //ex: BACHELOR, MASTER, TECHNICAL

    /**
     * Type of the course.
     */
    @Column(nullable = false, length = 50)
    private Type type; //ex: DAYTIME, EVENING, POST_WORK

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
