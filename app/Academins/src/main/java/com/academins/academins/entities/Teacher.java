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
 * Entity representing a Teacher in the academic system.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Teacher {

    /**
     * Primary key for the Teacher entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacherId;


    //Foreign Key to User entity on IAM microservice
    /**
     * Foreign key referencing the User entity in the IAM microservice.
     */
    @Column(name = "userId", nullable = false)
    private String userId;

    /**
     * Unique worker number assigned to the teacher.
     */
    @Column(name = "workerNumber", nullable = false, unique = true)
    private Integer workerNumber;

    /**
     * Full name of the teacher.
     */
    @Column(name = "fullName", nullable = false, length = 255)
    private String fullName;

    /**
     * Email address of the teacher.
     */
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
