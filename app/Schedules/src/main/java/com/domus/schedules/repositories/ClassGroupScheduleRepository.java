package com.domus.schedules.repositories;

import com.domus.schedules.entities.ClassGroupSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for ClassGroupSchedule aggregate root.
 * Provides access to schedules for class groups in course units.
 */
@Repository
public interface ClassGroupScheduleRepository extends JpaRepository<ClassGroupSchedule, Long> {

    @Query("SELECT DISTINCT cgs FROM ClassGroupSchedule cgs " +
            "INNER JOIN Lesson l ON l.classGroupSchedule.id = cgs.id " +
            "WHERE l.classroom.id = :classroomId")
    List<ClassGroupSchedule> findByClassroomId(Long classroomId);
}

