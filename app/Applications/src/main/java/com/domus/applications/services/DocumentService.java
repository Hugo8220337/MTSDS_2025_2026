package com.domus.applications.services;

import com.domus.applications.entities.Application;
import com.domus.applications.entities.ApplicationDocument;
import com.domus.applications.repositories.ApplicationRepository;
import com.domus.applications.repositories.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class DocumentService {

    private final ApplicationRepository applicationRepository;
    private final DocumentRepository documentRepository;

    public DocumentService(ApplicationRepository applicationRepository, DocumentRepository documentRepository) {
        this.applicationRepository = applicationRepository;
        this.documentRepository = documentRepository;
    }


    public ApplicationDocument uploadDocument(Long applicationId, String documentType, MultipartFile file) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(
                () -> new IllegalArgumentException("Application not found with id: " + applicationId)
        );

        String base64 = null;
        try {
            base64 = Base64.getEncoder().encodeToString(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the sent file.", e);
        }

        ApplicationDocument document = new ApplicationDocument(
                application,
                documentType,
                base64
        );

        return documentRepository.save(document);
    }

    public List<ApplicationDocument> listDocuments(Long applicationId) {
        return documentRepository.findByApplicationId(applicationId);
    }

    public ApplicationDocument getDocument(Long applicationId, Long documentId) {
        return documentRepository.findByIdAndApplicationId(documentId, applicationId);
    }

    public void deleteDocument(Long applicationId, Long documentId) {
        ApplicationDocument document = documentRepository.findByIdAndApplicationId(documentId, applicationId);
        if (document != null) {
            documentRepository.delete(document);
        } else {
            throw new IllegalArgumentException("Document not found with id: " + documentId + " for application id: " + applicationId);
        }
    }
}
