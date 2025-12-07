package com.domus.schedules.repositories;

import com.domus.schedules.entities.ScheduleException;
import com.domus.schedules.entities.Lesson;
import com.domus.schedules.valueObjects.ExceptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for ScheduleException entity.
 * Exceptions are part of the ClassGroupSchedule aggregate.
 */
@Repository
public interface ScheduleExceptionRepository extends JpaRepository<ScheduleException, Long> {

    @Query("SELECT se FROM ScheduleException se WHERE se.alternativeClassroom.id = :classroomId " +
            "AND se.date BETWEEN :startDate AND :endDate")
    List<ScheduleException> getExceptionsByClassroomIdAndDateRange(
            @Param("classroomId") Long classroomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<ScheduleException> findByDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT se FROM ScheduleException se WHERE se.lesson.id = :lessonId")
    List<ScheduleException> findByLessonId(Long lessonId);

    List<ScheduleException> findByAlternativeClassroomId(Long alternativeClassroomId);

    @Query("""
                SELECT se
                FROM ScheduleException se
                JOIN se.lesson l
                JOIN l.classroom c
                WHERE c.schoolId = :schoolId
                  AND se.date = :date
            """)
    List<ScheduleException> findBySchoolIdAndDate(Long schoolId, LocalDate date);
}

