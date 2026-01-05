package com.academins.academins.entities;

import jakarta.persistence.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a School in the academic system.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class School {

    /**
     * Primary key for the School entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long schoolId;

    /**
     * Name of the school.
     */
    @Column(nullable = false, length = 255)
    private String name;

    /**
     * Acronym of the school.
     */
    @Column(nullable = false, length = 20)
    private String acronym;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
