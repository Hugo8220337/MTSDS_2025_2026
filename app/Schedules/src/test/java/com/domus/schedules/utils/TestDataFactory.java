package com.domus.schedules.utils;

import com.domus.schedules.entities.*;
import com.domus.schedules.repositories.*;
import com.domus.schedules.valueObjects.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class TestDataFactory {
    public static Classroom insertClassroom(ClassroomRepository classroomRepository) {
        Classroom classroom = new Classroom(
                new ClassroomLocation("Building B " + System.currentTimeMillis(), 2, "Room 202"),
                ClassroomType.LABORATORY,
                10,
                1L
        );
        return classroomRepository.save(classroom);
    }

    public static Equipment insertEquipment(EquipmentRepository equipmentRepository) {
        Equipment equipment = new Equipment(
                "Projector" + System.currentTimeMillis(),
                "HD Projector" + System.currentTimeMillis()
        );
        return equipmentRepository.save(equipment);
    }

    public static ClassroomEquipment insertClassroomEquipment(
            ClassroomEquipmentRepository classroomEquipmentRepository,
            ClassroomRepository classroomRepository,
            EquipmentRepository equipmentRepository,
            Long equipmentId,
            Long classroomId
    ) {
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow();
        Equipment equipment = equipmentRepository.findById(equipmentId).orElseThrow();

        ClassroomEquipment classroomEquipment = new ClassroomEquipment(
                classroom,
                equipment,
                20,
                "(╯'□')╯︵ ┻━┻"
        );

        return classroomEquipmentRepository.save(classroomEquipment);
    }


    public static ClassGroupSchedule insertClassSchedule(
            ClassGroupScheduleRepository repo
    ) {
        ClassGroupSchedule s = new ClassGroupSchedule(
                (long) (Math.random() * (5 - 1)) + 1,
                (long) (Math.random() * (5 - 1)) + 1,
                (long) (Math.random() * (5 - 1)) + 1,
                new AcademicPeriod(
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 6, 30),
                        (int) (Math.random() * (5 - 1)) + 1
                )
        );
        return repo.save(s);
    }

    public static Lesson insertLesson(
            LessonRepository lessonRepo,
            ClassGroupScheduleRepository scheduleRepo,
            ClassroomRepository classroomRepository,
            Long classroomId
    ) {
        ClassGroupSchedule schedule = insertClassSchedule(scheduleRepo);
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow();
        Lesson lesson = new Lesson(
                schedule,
                1L,
                classroom,
                DayOfWeek.MONDAY,
                new TimeSlot(
                        LocalTime.parse("08:00"),
                        LocalTime.parse("09:00")
                ),
                LessonType.THEORETICAL
        );
        lesson.setClassGroupSchedule(schedule);
        return lessonRepo.save(lesson);
    }

}
