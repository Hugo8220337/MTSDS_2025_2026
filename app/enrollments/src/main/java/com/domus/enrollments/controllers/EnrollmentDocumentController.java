package com.domus.enrollments.controllers;

import com.domus.enrollments.dto.response.EnrollmentDocumentResponseDTO;
import com.domus.enrollments.entities.EnrollmentDocument;
import com.domus.enrollments.services.EnrollmentDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentDocumentController {

    private final ModelMapper mapper;
    private final EnrollmentDocumentService enrollmentDocumentService;

    @Autowired
    public EnrollmentDocumentController(ModelMapper mapper, EnrollmentDocumentService enrollmentDocumentService) {
        this.mapper = mapper;
        this.enrollmentDocumentService = enrollmentDocumentService;
    }


    /**
     * Upload a document for a specific Enrollment
     *
     * @param enrollmentId The ID of the Enrollment
     * @param file         The MultipartFile representing the document to be uploaded
     * @param documentType The type of the document being uploaded
     * @return ResponseEntity containing the uploaded EnrollmentDocument DTO and HTTP status
     */
    @PostMapping(value = "/{enrollmentId}/upload-document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EnrollmentDocumentResponseDTO> uploadEnrollmentDocument(
            @PathVariable Long enrollmentId,
            @RequestPart String documentType,
            @RequestPart MultipartFile file) {
        log.info("Upload enrollment document");

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file cannot be null or empty");
        }

        EnrollmentDocument document = enrollmentDocumentService.uploadEnrollmentDocument(enrollmentId, file, documentType);
        EnrollmentDocumentResponseDTO responseDTO = mapper.map(document, EnrollmentDocumentResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Get all documents for a specific Enrollment
     *
     * @param enrollmentId The ID of the Enrollment
     * @return ResponseEntity containing the list of EnrollmentDocument DTOs and HTTP status
     */
    @GetMapping("/{enrollmentId}/documents")
    public ResponseEntity<List<EnrollmentDocumentResponseDTO>> getEnrollmentDocuments(@PathVariable Long enrollmentId) {
        log.info("Recieved request to get documents for enrollment ID: {}", enrollmentId);

        List<EnrollmentDocument> documents = enrollmentDocumentService.getEnrollmentDocuments(enrollmentId);
        List<EnrollmentDocumentResponseDTO> documentResponse = documents.stream()
                .map(document -> mapper.map(document, EnrollmentDocumentResponseDTO.class))
                .toList();
        return ResponseEntity.ok(documentResponse);
    }
}


