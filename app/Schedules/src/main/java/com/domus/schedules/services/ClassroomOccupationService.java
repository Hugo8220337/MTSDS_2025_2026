package com.domus.schedules.services;

import com.domus.schedules.dto.*;
import com.domus.schedules.dto.response.ClassroomOccupationResponseDTO;
import com.domus.schedules.entities.Classroom;
import com.domus.schedules.entities.Lesson;
import com.domus.schedules.entities.Reservation;
import com.domus.schedules.entities.ScheduleException;
import com.domus.schedules.repositories.ClassroomRepository;
import com.domus.schedules.repositories.LessonRepository;
import com.domus.schedules.repositories.ReservationRepository;
import com.domus.schedules.repositories.ScheduleExceptionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClassroomOccupationService {
    private final ModelMapper mapper;
    private final ClassroomRepository classroomRepository;
    private final LessonRepository lessonRepository;
    private final ScheduleExceptionRepository scheduleExceptionRepository;
    private final ReservationRepository reservationRepository;

    public ClassroomOccupationService(
            ModelMapper mapper, ClassroomRepository classroomRepository,
            LessonRepository lessonRepository,
            ScheduleExceptionRepository scheduleExceptionRepository,
            ReservationRepository reservationRepository
    ) {
        this.mapper = mapper;
        this.classroomRepository = classroomRepository;
        this.lessonRepository = lessonRepository;
        this.scheduleExceptionRepository = scheduleExceptionRepository;
        this.reservationRepository = reservationRepository;
    }

    public ClassroomOccupationResponseDTO getOccupation(Long schoolId, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // Search for classrooms
        List<Classroom> classrooms = classroomRepository.findBySchoolId(schoolId);

        // Search for reservations
        List<Reservation> reservations =
                reservationRepository.findBySchoolIdAndDate(schoolId, date);
        // Search for schedule exceptions
        List<ScheduleException> exceptions =
                scheduleExceptionRepository.findBySchoolIdAndDate(schoolId, date);
        // Search for regular lessons
        List<Lesson> lessons =
                lessonRepository.findBySchoolIdAndDayOfWeek(schoolId, dayOfWeek);

        // Create DTO for Classroom details
        List<ClassroomOccupationResponseDTO.ClassroomDetailDTO> details = classrooms.stream()
                .map(cr -> buildDetailDTO(cr, lessons, exceptions, reservations))
                .toList();

        return ClassroomOccupationResponseDTO.builder()
                .schoolId(schoolId)
                .date(date)
                .dayOfWeek(dayOfWeek)
                .classrooms(details.toArray(new ClassroomOccupationResponseDTO.ClassroomDetailDTO[0]))
                .build();
    }

    private ClassroomOccupationResponseDTO.ClassroomDetailDTO buildDetailDTO(
            Classroom classroom,
            List<Lesson> lessons,
            List<ScheduleException> exceptions,
            List<Reservation> reservations
    ) {
        List<ClassroomOccupationResponseDTO.ClassroomOccupationDetailDTO> occupationList = new ArrayList<>();

        // Normal Lessons
        lessons.stream()
                .filter(l -> l.getClassroom().getId().equals(classroom.getId())) //
                .forEach(l -> {

                    ScheduleException ex = exceptions.stream()
                            .filter(e -> e.getLesson().getId().equals(l.getId()))
                            .findFirst()
                            .orElse(null);

                    occupationList.add(buildLessonOccupation(l, ex, classroom));
                });

        // Reservations for this classroom
        reservations.stream()
                .filter(r -> r.getClassroom().getId().equals(classroom.getId()))
                .forEach(r -> occupationList.add(buildReservationOccupation(r, classroom)));

        // Exceptions that moved to this classroom
        exceptions.stream()
                .filter(ex -> ex.getAlternativeClassroom() != null &&
                        ex.getAlternativeClassroom().getId().equals(classroom.getId()))
                .forEach(ex -> occupationList.add(buildExceptionOccupation(ex, classroom)));

        return ClassroomOccupationResponseDTO.ClassroomDetailDTO.builder()
                .id(classroom.getId())
                .type(classroom.getType())
                .capacity(classroom.getCapacity())
                .occupations(occupationList.toArray(new ClassroomOccupationResponseDTO.ClassroomOccupationDetailDTO[0]))
                .build();
    }

    /**
     * Builds a ClassroomOccupationDetailDTO for a lesson, considering any schedule exception.
     * @param lesson lesson being held
     * @param ex schedule exception, if any
     * @param classroom classroom where the lesson is held
     * @return DTO representing the occupation detail
     */
    private ClassroomOccupationResponseDTO.ClassroomOccupationDetailDTO buildLessonOccupation(
            Lesson lesson,
            ScheduleException ex,
            Classroom classroom
    ) {
        LocalTime start = (ex != null && ex.getAlternativeStartTime() != null) ?
                ex.getAlternativeStartTime() : lesson.getTimeSlot().getStartTime();

        LocalTime end = (ex != null && ex.getAlternativeEndTime() != null) ?
                ex.getAlternativeEndTime() : lesson.getTimeSlot().getEndTime();

        return ClassroomOccupationResponseDTO.ClassroomOccupationDetailDTO.builder()
                .type(OccupationType.LESSON)
                .capacity(classroom.getCapacity())
                .teacherId(
                        ex != null && ex.getSubstituteInstructorId() != null
                                ? ex.getSubstituteInstructorId().toString()
                                : lesson.getInstructorId().toString()
                )
                .timeSlot(TimeSlotDTO.builder()
                        .startTime(start)
                        .endTime(end)
                        .build())
                .build();
    }

    /**
     * Builds a ClassroomOccupationDetailDTO for a reservation.
     * @param r reservation for the classroom
     * @param c classroom being reserved
     * @return DTO representing the occupation detail
     */
    private ClassroomOccupationResponseDTO.ClassroomOccupationDetailDTO buildReservationOccupation(
            Reservation r, Classroom c) {

        return ClassroomOccupationResponseDTO.ClassroomOccupationDetailDTO.builder()
                .type(OccupationType.RESERVATION)
                .capacity(c.getCapacity())
                .teacherId(r.getResponsibleId() != null ? r.getResponsibleId().toString() : null)
                .timeSlot(TimeSlotDTO.builder()
                        .startTime(r.getStartTime())
                        .endTime(r.getEndTime())
                        .build())
                .build();
    }

    /**
     * Builds a ClassroomOccupationDetailDTO for a schedule exception.
     * @param ex schedule exception that moved to this classroom
     * @param c classroom being used
     * @return DTO representing the occupation detail
     */
    private ClassroomOccupationResponseDTO.ClassroomOccupationDetailDTO buildExceptionOccupation(
            ScheduleException ex, Classroom c) {

        return ClassroomOccupationResponseDTO.ClassroomOccupationDetailDTO.builder()
                .type(OccupationType.EXCEPTION)
                .capacity(c.getCapacity())
                .teacherId(
                        ex.getSubstituteInstructorId() != null
                                ? ex.getSubstituteInstructorId().toString()
                                : ex.getLesson().getInstructorId().toString()
                )
                .timeSlot(TimeSlotDTO.builder()
                        .startTime(ex.getAlternativeStartTime())
                        .endTime(ex.getAlternativeEndTime())
                        .build())
                .build();
    }
}
