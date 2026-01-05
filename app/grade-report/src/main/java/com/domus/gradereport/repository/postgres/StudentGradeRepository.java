package com.domus.gradereport.repository.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.domus.gradereport.model.postgres.StudentGrade;

import java.util.List;

@Repository
public interface StudentGradeRepository extends JpaRepository<StudentGrade, Long> {
    List<StudentGrade> findByGradeReportId(Long gradeReportId);
}