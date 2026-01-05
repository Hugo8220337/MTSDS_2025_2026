package com.domus.enrollments.services;

import com.domus.enrollments.entities.Enrollment;
import com.domus.enrollments.entities.EnrollmentDocument;
import com.domus.enrollments.repositories.EnrollmentDocumentRepository;
import com.domus.enrollments.repositories.EnrollmentRepository;
import com.domus.enrollments.valueObjects.DocumentState;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class EnrollmentDocumentService {

    private final ModelMapper mapper;
    private final EnrollmentDocumentRepository enrollmentDocumentRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentDocumentService(
            ModelMapper mapper,
            EnrollmentDocumentRepository enrollmentDocumentRepository,
            EnrollmentRepository enrollmentRepository) {
        this.mapper = mapper;
        this.enrollmentDocumentRepository = enrollmentDocumentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * Upload a document for a specific Enrollment
     *
     * @param enrollmentId The ID of the Enrollment
     * @param file         The MultipartFile representing the document to be uploaded
     * @param documentType The type of the document being uploaded
     * @return The saved EnrollmentDocument entity
     * @throws IllegalArgumentException if the Enrollment with the given ID does not exist
     */
    public EnrollmentDocument uploadEnrollmentDocument(Long enrollmentId, MultipartFile file, String documentType) {

        Enrollment enrollmet = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment with ID " + enrollmentId + " not found."));

        String encodedFile;
        try {
            encodedFile = Base64.getEncoder().encodeToString(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file bytes", e);
        }

        EnrollmentDocument document = new EnrollmentDocument(enrollmet, documentType, encodedFile, DocumentState.PENDING);

        return enrollmentDocumentRepository.save(document);
    }

    /**
     * Get all documents for a specific Enrollment
     *
     * @param enrollmentId The ID of the Enrollment
     * @return List of EnrollmentDocument entities
     */
    @Transactional(readOnly = true) // Ensure no lob errors during lazy loading
    public List<EnrollmentDocument> getEnrollmentDocuments(Long enrollmentId) {
        List<EnrollmentDocument> enrollmentDocuments = enrollmentDocumentRepository.findByEnrollmentEnrollmentId(enrollmentId).orElseThrow(
                () -> new IllegalArgumentException("No documents found for Enrollment with ID " + enrollmentId)
        );
        return enrollmentDocuments;

    }
}
