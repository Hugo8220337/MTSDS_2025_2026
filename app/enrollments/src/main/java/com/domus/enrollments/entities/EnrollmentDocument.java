package com.domus.enrollments.entities;


import com.domus.enrollments.valueObjects.DocumentState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing an Enrollment Document.
 */
@Getter
@Setter
@Entity
public class EnrollmentDocument {

    /**
     * Primary key for the EnrollmentDocumentRepository entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentDocumentId;
    /**
     * Foreign key referencing the Enrollment entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "enrollment_id", nullable = false)
    private Enrollment enrollment;

    /**
     * Type of the document (e.g., ID_PROOF, ADDRESS_PROOF).
     */
    @Column(nullable = false)
    private String documentType;

    /**
     * Content of the document.
     */
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String documentContent;

//    /**
//     * Path to the document file.
//     */
//   @Column(nullable = false)
//    private String documentPath;



    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    protected  EnrollmentDocument() {} // JPA requirement

    public EnrollmentDocument(Enrollment enrollment, String documentType, String encodedFile, DocumentState documentState) {
        this.enrollment = enrollment;
        this.documentType = documentType;
        this.documentContent = encodedFile;
    }

}
