package com.domus.schedules.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * Equipment entity representing types of equipment available in classrooms.
 * Immutable value-like entity with identity.
 */
@Entity
@Getter
@Setter
@Table(name = "equipment")
public class Equipment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private long version;


    protected Equipment() {}

    /**
     * Creates a new Equipment type.
     * @param name the equipment name (must be unique)
     * @param description optional description
     */
    public Equipment(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Updates the equipment description.
     * @param newDescription the new description
     */
    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
     * Renames the equipment.
     * @param newName the new name
     * @throws IllegalArgumentException if name is invalid
     */
    public void rename(String newName) {
        this.name = newName;
    }
}

