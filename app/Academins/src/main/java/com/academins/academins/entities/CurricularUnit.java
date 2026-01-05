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
 * Entity representing a Curricular Unit in the academic system.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "curricular_units")
@Entity
public class CurricularUnit {

    /**
     * Primary key for the CurricularUnit entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curricular_unit_id", insertable = false, updatable = false)
    private Long curricularUnitId;

    /**
    * Name of the curricular unit.
    */
    @Column(nullable = false, length = 255)
    private String name;

    /**
     * Code of the curricular unit.
     */
    @Column(nullable = false, length = 50, unique = true)
    private String codeCU;

    /**
     * Number of credits for the curricular unit.
     */
    @Column(nullable = false)
    private int credits;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
