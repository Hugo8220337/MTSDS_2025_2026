package com.domus.assessments.repository.postgres;

import com.domus.assessments.model.postgres.GradeReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<GradeReview, Long> {
    List<GradeReview> findByStudentId(Long studentId);
    List<GradeReview> findByStudentGradeId(Long gradeId);
}