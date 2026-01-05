package com.domus.applications.repositories;

import com.domus.applications.entities.ApplicationDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<ApplicationDocument, Long> {
    List<ApplicationDocument> findByApplicationId(Long applicationId);

    @Query("SELECT d FROM ApplicationDocument d WHERE d.id = :documentId AND d.application.id = :applicationId")
    ApplicationDocument findByIdAndApplicationId(Long documentId, Long applicationId);
}
