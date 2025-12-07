package com.domus.schedules.repositories;

import com.domus.schedules.entities.ClassGroupSchedule;
import com.domus.schedules.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

/**
 * Repository for Lesson entity.
 * Lessons are part of the ClassGroupSchedule aggregate.
 */
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("SELECT l FROM Lesson l WHERE l.classGroupSchedule.id = :id")
    List<Lesson> findByClassGroupScheduleId(Long id);

    @Query("SELECT l FROM Lesson l WHERE l.classGroupSchedule = :schedule")
    List<Lesson> findByClassGroupSchedule(ClassGroupSchedule schedule);

    @Query("""
                SELECT l
                FROM Lesson l
                JOIN l.classroom c
                WHERE c.schoolId = :schoolId
                  AND l.dayOfWeek = :dayOfWeek
            """)
    List<Lesson> findBySchoolIdAndDayOfWeek(Long schoolId, DayOfWeek dayOfWeek);
}

