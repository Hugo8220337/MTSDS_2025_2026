package com.domus.assessments.repository.postgres;

import com.domus.assessments.model.postgres.StudentGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<StudentGrade, Long> {
    List<StudentGrade> findByStudentId(Long studentId);

    List<StudentGrade> findByEvaluationMomentId(Long evaluationMomentId);

    Optional<StudentGrade> findByStudentIdAndEvaluationMomentId(Long studentId, Long evaluationMomentId);
}