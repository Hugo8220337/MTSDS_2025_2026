package com.domus.schedules.services;

import com.domus.schedules.client.AcademinsApiClient;
import com.domus.schedules.dto.response.ClassroomAvailabilityResponseDTO;
import com.domus.schedules.dto.request.ClassroomRequestDTO;
import com.domus.schedules.entities.*;
import com.domus.schedules.exceptions.ResourceNotFoundException;
import com.domus.schedules.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClassroomService{

    private final ModelMapper mapper;
    private final AcademinsApiClient academinsApiClient;
    private final ClassroomRepository classroomRepository;
    private final ScheduleExceptionRepository scheduleExceptionRepository;
    private final ReservationRepository reservationRepository;
    private final ClassGroupScheduleRepository classGroupScheduleRepository;
    private final LessonRepository lessonRepository;
    private final ClassroomEquipmentRepository classroomEquipmentRepository;

    public ClassroomService(ModelMapper mapper, AcademinsApiClient academinsApiClient, ClassroomRepository classroomRepository, ScheduleExceptionRepository scheduleExceptionRepository, ReservationRepository reservationRepository, ClassGroupScheduleRepository classGroupScheduleRepository, LessonRepository lessonRepository, ClassroomEquipmentRepository classroomEquipmentRepository) {
        this.mapper = mapper;
        this.academinsApiClient = academinsApiClient;
        this.classroomRepository = classroomRepository;
        this.scheduleExceptionRepository = scheduleExceptionRepository;
        this.reservationRepository = reservationRepository;
        this.classGroupScheduleRepository = classGroupScheduleRepository;
        this.lessonRepository = lessonRepository;
        this.classroomEquipmentRepository = classroomEquipmentRepository;
    }


    public Classroom createClassroom(Long schoolId, ClassroomRequestDTO req) {
        if (!academinsApiClient.existsSchool(req.getSchoolId())) {
            throw new ResourceNotFoundException("School does not exist.");
        }

        req.setSchoolId(schoolId);
        Classroom classroom = mapper.map(req, Classroom.class);
        return classroomRepository.save(classroom);
    }

    public ClassroomAvailabilityResponseDTO getClassroomAvailability(Long classroomId, LocalDate startDate, LocalDate endDate) {
        // Get Reservations
        List<Reservation> reservationsForTheClassroom = reservationRepository.getReservationsByClassroomIdAndDateTimeRange(classroomId, startDate, endDate);

        // Get Schedule Exceptions
        List<ScheduleException> exceptionsForTheClassroom = scheduleExceptionRepository.getExceptionsByClassroomIdAndDateRange(classroomId, startDate, endDate);

        // Get Recurring Schedules
        List<ClassGroupSchedule> recurringSchedules = classGroupScheduleRepository
                .findByClassroomId(classroomId);
        List<LocalDateTime> recurringOccurrences = expandRecurringSchedules(
                recurringSchedules,
                startDate,
                endDate,
                exceptionsForTheClassroom
        );

        // Combine recurring occurrences and reservations
        List<LocalDateTime> busySlots = new ArrayList<>(recurringOccurrences);
        reservationsForTheClassroom.forEach(r -> busySlots.add(r.getStartDateTime()));

        // Calcular availableSlots
        List<LocalDateTime> availableSlots = calculateAvailableSlots(startDate, endDate, busySlots);

        return ClassroomAvailabilityResponseDTO.builder()
                .classroomId(classroomId)
                .busySlots(busySlots)
                .availableSlots(availableSlots)
                .build();
    }

    private List<LocalDateTime> calculateAvailableSlots(LocalDate startDate, LocalDate endDate, List<LocalDateTime> busySlots) {
        List<LocalDateTime> available = new ArrayList<>();
        LocalTime startTime = LocalTime.of(8, 0);   // start of the day
        LocalTime endTime = LocalTime.of(20, 0);    // end of the day
        int slotMinutes = 30; // duration of each slot in minutes

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(slotMinutes)) {
                LocalDateTime slot = LocalDateTime.of(date, time);
                if (!busySlots.contains(slot)) {
                    available.add(slot);
                }
            }
        }
        return available;
    }

    private List<LocalDateTime> expandRecurringSchedules(
            List<ClassGroupSchedule> schedules,
            LocalDate startDate,
            LocalDate endDate,
            List<ScheduleException> exceptions) {

        List<LocalDateTime> occurrences = new ArrayList<>();

        for (ClassGroupSchedule schedule : schedules) {
            // Get lessons for the schedule
            List<Lesson> lessons = lessonRepository.findByClassGroupScheduleId(schedule.getId());

            for (Lesson lesson : lessons) {
                // Iterate through the date range
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    final LocalDate currentDate = date;

                    // Verify if the lesson occurs on this day of the week
                    if (currentDate.getDayOfWeek() == lesson.getDayOfWeek()) {
                        // Verify if the schedule is active on this date
                        if (schedule.isActiveOn(currentDate)) {
                            boolean hasException = exceptions.stream()
                                    .anyMatch(ex -> ex.getDate().equals(currentDate));

                            if (!hasException) {
                                occurrences.add(LocalDateTime.of(currentDate, lesson.getTimeSlot().getStartTime()));
                            }
                        }
                    }
                }
            }
        }

        return occurrences;
    }

    public List<Equipment> getClassroomEquipment(Long classroomId) {
        return classroomEquipmentRepository.findByClassroomId(classroomId);
    }
}
