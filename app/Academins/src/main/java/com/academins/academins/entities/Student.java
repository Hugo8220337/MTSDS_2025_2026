package com.academins.academins.entities;

import com.academins.academins.valueObjects.StudentState;
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
 * Entity representing a Student.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Student {

    /**
     * Primary key for the Student entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    //Foreign Key to User entity on IAM microservice
    /**
     * Foreign key referencing the User entity in the IAM microservice.
     */
    @Column(nullable = false)
    private String userId;

    /**
     * Unique student number assigned to the student.
     */
    @Column(nullable = false, unique = true)
    private String studentNumber;

    /**
     * Many-to-one relationship with the Course entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "course_id", nullable = false)
    private Course course;

    /**
     * State of the student (e.g., ACTIVE, INACTIVE).
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudentState studentState;

    /**
     * Full name of the student.
     */
    @Column(nullable = false, length = 255)
    private String fullName;

    /**
     * Date of birth of the student.
     */
    @Column(nullable = false)
    private String dateOfBirth;

    /**
     * Fiscal Identification Number of the student.
     */
    @Column(nullable = false, unique = true, length = 9)
    private String fin; //Fiscal Identification Number

    /**
     * Telephone number of the student.
     */
    @Column(nullable = false, unique = true, length = 9)
    private String telephoneNumber;

    /**
     * Email address of the student.
     */
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /**
     * Many-to-one relationship with the AcademyClass entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academy_class_id")
    private AcademyClass academyClass;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
