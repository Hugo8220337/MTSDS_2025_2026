package com.domus.gradereport.repository.postgres;

import com.domus.gradereport.model.postgres.GradeReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeReportRepository extends JpaRepository<GradeReport, Long> {
    List<GradeReport> findByCurricularUnitId(Long curricularUnitId);
    List<GradeReport> findByTeacherId(Long teacherId);
}