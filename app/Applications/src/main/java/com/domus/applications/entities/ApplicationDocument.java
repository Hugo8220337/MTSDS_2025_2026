package com.domus.applications.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "application_documents")
public class ApplicationDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "document_type", nullable = false)
    private String documentType;

//    @Column(name = "document_url", nullable = false)
//    private String documentUrl;

    @Column(name = "document_base_64", columnDefinition = "TEXT", nullable = false)
    private String documentBase64;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    protected ApplicationDocument() {} // JPA

    public ApplicationDocument(Application application, String documentType, String documentBase64) {
        this.application = application;
        this.documentType = documentType;
//        this.documentUrl = documentUrl;
        this.documentBase64 = documentBase64;
    }
}