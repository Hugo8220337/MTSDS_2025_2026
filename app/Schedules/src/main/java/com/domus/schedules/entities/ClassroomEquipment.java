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
 * ClassroomEquipment entity representing the association between classrooms and equipment.
 * Part of the Classroom aggregate.
 */
@Entity
@Getter
@Setter
@Table(name = "classroom_equipment", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"classroom_id", "equipment_id"})
})
public class ClassroomEquipment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @Column(nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private long version;


    protected ClassroomEquipment() {}

    /**
     * Creates a new ClassroomEquipment association.
     * @param classroom the classroom
     * @param equipment the equipment
     * @param quantity number of units
     * @param observations optional notes
     */
    public ClassroomEquipment(Classroom classroom, Equipment equipment, Integer quantity, String observations) {
        this.classroom = classroom;
        this.equipment = equipment;
        this.quantity = quantity;
        this.observations = observations;
    }

    /**
     * Updates the quantity of this equipment.
     * @param newQuantity the new quantity
     * @throws IllegalArgumentException if quantity is invalid
     */
    public void updateQuantity(Integer newQuantity) {
        this.quantity = newQuantity;
    }

    /**
     * Adds observations about this equipment.
     * @param additionalObservations notes to add
     */
    public void addObservations(String additionalObservations) {
        if (this.observations == null || this.observations.isBlank()) {
            this.observations = additionalObservations;
        } else {
            this.observations += "\n" + additionalObservations;
        }
    }

    /**
     * Clears all observations.
     */
    public void clearObservations() {
        this.observations = null;
    }

    /**
     * Checks if this equipment is functional (has no critical observations).
     * @return true if functional
     */
    public boolean isFunctional() {
        if (observations == null) return true;
        String lower = observations.toLowerCase();
        return !lower.contains("broken") &&
               !lower.contains("defeito") &&
               !lower.contains("out of order");
    }
}

