package com.domus.applications.controllers;

import com.domus.applications.dto.response.DocumentResponseDto;
import com.domus.applications.entities.ApplicationDocument;
import com.domus.applications.services.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/applications/{applicationId}/documents")
public class DocumentsController {

    private final ModelMapper mapper;
    private final DocumentService documentService;

    public DocumentsController(ModelMapper mapper, DocumentService documentService) {
        this.mapper = mapper;
        this.documentService = documentService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponseDto> uploadDocument(
            @PathVariable Long applicationId,
            @RequestParam String documentType,
            @RequestParam MultipartFile file) {
        log.info("Uploading document for applicationId: {}", applicationId);

        ApplicationDocument responseDto = documentService.uploadDocument(applicationId, documentType, file);
        DocumentResponseDto mappedDto = mapper.map(responseDto, DocumentResponseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(mappedDto);
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponseDto>> listDocuments(
            @PathVariable Long applicationId) {
        log.info("Listing documents for applicationId: {}", applicationId);

        List<ApplicationDocument> documents = documentService.listDocuments(applicationId);
        List<DocumentResponseDto> responseDtos = documents.stream()
                .map(doc -> mapper.map(doc, DocumentResponseDto.class))
                .toList();
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentResponseDto> getDocument(
            @PathVariable Long applicationId,
            @PathVariable Long documentId) {
        log.info("Getting document with id: {} for applicationId: {}", documentId, applicationId);

        ApplicationDocument document = documentService.getDocument(applicationId, documentId);
        DocumentResponseDto responseDto = mapper.map(document, DocumentResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long applicationId,
            @PathVariable Long documentId) {
        log.info("Deleting document with id: {} for applicationId: {}", documentId, applicationId);

        documentService.deleteDocument(applicationId, documentId);
        return ResponseEntity.noContent().build();
    }
}