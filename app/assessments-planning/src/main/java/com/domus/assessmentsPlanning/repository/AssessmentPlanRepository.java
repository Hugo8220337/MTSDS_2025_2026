package com.domus.assessmentsPlanning.repository;

import com.domus.assessmentsPlanning.model.postgres.AssessmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssessmentPlanRepository extends JpaRepository<AssessmentPlan, Long> {
    List<AssessmentPlan> findByCourseUnitId(Long courseUnitId);
    List<AssessmentPlan> findByAcademicYear(String academicYear);
}