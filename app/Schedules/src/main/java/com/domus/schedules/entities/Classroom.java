package com.domus.schedules.entities;

import com.domus.schedules.valueObjects.ClassroomLocation;
import com.domus.schedules.valueObjects.ClassroomType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * Classroom aggregate root representing physical spaces where lessons take place.
 * Manages its own invariants and provides business operations.
 */
@Entity
@Getter
@Setter
@Table(name = "classroom", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"building", "floor", "roomNumber"})
})
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "building", column = @Column(name = "building", nullable = false, length = 50)),
        @AttributeOverride(name = "floor", column = @Column(name = "floor", nullable = false)),
        @AttributeOverride(name = "roomNumber", column = @Column(name = "room_number", nullable = false, length = 20))
    })
    private ClassroomLocation location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ClassroomType type;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "school_id", nullable = false)
    private Long schoolId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private long version;


    protected Classroom() {}

    /**
     * Creates a new Classroom with all required information.
     * Validates all invariants.
     */
    public Classroom(ClassroomLocation location, ClassroomType type, Integer capacity, Long schoolId) {
        this.location = location;
        this.type = type;
        this.capacity = capacity;
        this.schoolId = schoolId;
    }

    /**
     * Changes the physical location of the classroom.
     * @param newLocation the new location
     * @throws IllegalArgumentException if location is invalid
     */
    public void relocate(ClassroomLocation newLocation) {
        this.location = newLocation;
    }

    /**
     * Updates the classroom capacity.
     * @param newCapacity the new capacity
     * @throws IllegalArgumentException if capacity is invalid
     */
    public void updateCapacity(Integer newCapacity) {
        this.capacity = newCapacity;
    }

    /**
     * Changes the classroom type.
     * @param newType the new type
     * @throws IllegalArgumentException if type is invalid
     */
    public void changeType(ClassroomType newType) {
        this.type = newType;
    }

    /**
     * Checks if this classroom can accommodate a given number of people.
     * @param requiredCapacity the number of people
     * @return true if capacity is sufficient
     */
    public boolean canAccommodate(Integer requiredCapacity) {
        if (requiredCapacity == null || requiredCapacity <= 0) {
            return false;
        }
        return this.capacity >= requiredCapacity;
    }

    /**
     * Checks if this classroom is in a specific building.
     * @param building the building identifier
     * @return true if in the specified building
     */
    public boolean isInBuilding(String building) {
        return this.location.getBuilding().equalsIgnoreCase(building);
    }

    /**
     * Checks if this classroom is of a specific type.
     * @param type the classroom type
     * @return true if matches the type
     */
    public boolean isOfType(ClassroomType type) {
        return this.type == type;
    }
}
