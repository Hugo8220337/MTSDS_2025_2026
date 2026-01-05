package com.domus.enrollments.repositories;

import com.domus.enrollments.entities.EnrollmentDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentDocumentRepository extends JpaRepository<EnrollmentDocument, Long> {

    @Query("SELECT ed FROM EnrollmentDocument ed JOIN FETCH ed.enrollment WHERE ed.enrollment.enrollmentId = :enrollmentId")
    Optional<List<EnrollmentDocument>> findByEnrollmentEnrollmentId(Long enrollmentId);
}
