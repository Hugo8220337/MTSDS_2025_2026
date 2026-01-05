package com.domus.schedules.services;

import com.domus.schedules.client.AcademinsApiClient;
import com.domus.schedules.configs.MQConfig;
import com.domus.schedules.dto.request.LessonRequestDTO;
import com.domus.schedules.dto.request.ScheduleExceptionRequestDTO;
import com.domus.schedules.dto.response.StudentResponseDTO;
import com.domus.schedules.entities.ClassGroupSchedule;
import com.domus.schedules.entities.Classroom;
import com.domus.schedules.entities.Lesson;
import com.domus.schedules.entities.ScheduleException;
import com.domus.schedules.messages.SendNotificationMessage;
import com.domus.schedules.repositories.ClassGroupScheduleRepository;
import com.domus.schedules.repositories.ClassroomRepository;
import com.domus.schedules.repositories.LessonRepository;
import com.domus.schedules.repositories.ScheduleExceptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ScheduleExceptionService {
    private final ModelMapper mapper;
    private final RabbitTemplate rabbitTemplate;
    private final AcademinsApiClient academinsApiClient;
    private final ScheduleExceptionRepository scheduleExceptionRepository;
    private final LessonRepository lessonRepository;
    private final ClassroomRepository classroomRepository;
    private final ClassGroupScheduleRepository classGroupScheduleRepository;

    public ScheduleExceptionService(ModelMapper mapper, RabbitTemplate rabbitTemplate, AcademinsApiClient academinsApiClient, ScheduleExceptionRepository scheduleExceptionRepository, LessonRepository lessonRepository, ClassroomRepository classroomRepository, ClassGroupScheduleRepository classGroupScheduleRepository) {
        this.mapper = mapper;
        this.rabbitTemplate = rabbitTemplate;
        this.academinsApiClient = academinsApiClient;
        this.scheduleExceptionRepository = scheduleExceptionRepository;
        this.lessonRepository = lessonRepository;
        this.classroomRepository = classroomRepository;
        this.classGroupScheduleRepository = classGroupScheduleRepository;
    }

    public ScheduleException createException(ScheduleExceptionRequestDTO dto) {

        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Lesson with ID " + dto.getLessonId() + " not found"));

        ScheduleException exceptionEntity = mapper.map(dto, ScheduleException.class);
        exceptionEntity.setLesson(lesson); // Set relationships

        if (dto.getAlternativeClassroomId() != null) {
            Classroom classroom = classroomRepository.findById(dto.getAlternativeClassroomId())
                    .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
            exceptionEntity.setAlternativeClassroom(classroom);
        }

        // Save the new ScheduleException
        ScheduleException newScheduleException = scheduleExceptionRepository.save(exceptionEntity);

        // Send RabbitMQ message to Notreservaifications Service
        sendNotificationAboutScheduleException(dto);

        return newScheduleException;
    }

    public ScheduleException updateException(Long scheduleExceptionId, ScheduleExceptionRequestDTO dto) {
        ScheduleException existingException = scheduleExceptionRepository.findById(scheduleExceptionId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ScheduleException with ID " + scheduleExceptionId + " not found"));


        // Map only simple fields
        if (dto.getDate() != null) existingException.setDate(dto.getDate());
        if (dto.getType() != null) existingException.setType(dto.getType());
        if (dto.getObservations() != null) existingException.setObservations(dto.getObservations());
        if (dto.getAlternativeStartTime() != null)
            existingException.setAlternativeStartTime(dto.getAlternativeStartTime());
        if (dto.getAlternativeEndTime() != null) existingException.setAlternativeEndTime(dto.getAlternativeEndTime());
        if (dto.getSubstituteInstructorId() != null)
            existingException.setSubstituteInstructorId(dto.getSubstituteInstructorId());

        if (dto.getAlternativeClassroomId() != null) {
            Classroom classroom = classroomRepository.findById(dto.getAlternativeClassroomId())
                    .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
            existingException.setAlternativeClassroom(classroom);
        }

        // Save updated ScheduleException
        ScheduleException updatedScheduleException = scheduleExceptionRepository.save(existingException);

        // Send RabbitMQ message to Notifications Service
        sendNotificationAboutScheduleException(dto);

        return updatedScheduleException;
    }

    public List<ScheduleException> getExceptionsByLessonId(Long lessonId) {
        return scheduleExceptionRepository.findByLessonId(lessonId);
    }

    private void sendNotificationAboutScheduleException(ScheduleExceptionRequestDTO scheduleExceptionRequestDTO) {
        // Get Lesson
        Long lessonId = scheduleExceptionRequestDTO.getLessonId();
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                () -> new EntityNotFoundException("Lesson not found")
        );

        // Get ClassGroupSchedule
        Long classGroupScheduleId = lesson.getClassGroupSchedule().getId();
        ClassGroupSchedule classGroupSchedule = classGroupScheduleRepository.findById(classGroupScheduleId).orElseThrow(
                () -> new EntityNotFoundException("ClassGroupSchedule not found")
        );


        // Get Students in Class from Academins API
        Long classGroupId = classGroupSchedule.getClassGroupId();
        List<StudentResponseDTO> studentsInClass = academinsApiClient.getStudentsInClass(classGroupId);

        // Prepare and send notification message
        for (StudentResponseDTO student : studentsInClass) {
            // Fetch full student details to get email
            String messageBody = String.format(
                    "Dear %s,\n" +
                            "There is a schedule exception for your class on %s. Please check the details.\n" +
                            "With start time %s and end time %s\n\n" +
                            "Best regards\n\n," +
                            "Do not reply, this is an automatic message.",
                    student.getFullName(),
                    scheduleExceptionRequestDTO.getAlternativeStartTime(),
                    scheduleExceptionRequestDTO.getAlternativeEndTime(),
                    scheduleExceptionRequestDTO.getDate().toString()
            );
            SendNotificationMessage message = SendNotificationMessage.builder()
                    .userId(student.getUserId())
                    .email(student.getEmail())
                    .title("Schedule Exception Notification")
                    .message(messageBody)
                    .build();
            rabbitTemplate.convertAndSend(
                    MQConfig.EXCHANGE,
                    MQConfig.ROUTING_KEY,
                    message
            );
            log.debug("Sent notification message to student ID: {}", student.getStudentId());
        }
    }

    public Lesson createPeriodicLesson(Long scheduleId, LessonRequestDTO request) {
        ClassGroupSchedule schedule = classGroupScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found: " + scheduleId));

        Classroom classroom = classroomRepository.findById(request.getClassroomId())
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));

        request.setClassGroupScheduleId(scheduleId);
        Lesson newLesson = mapper.map(request, Lesson.class);

        // Set relationships
        newLesson.setClassGroupSchedule(schedule);
        newLesson.setClassroom(classroom);

        return lessonRepository.save(newLesson);
    }
}
