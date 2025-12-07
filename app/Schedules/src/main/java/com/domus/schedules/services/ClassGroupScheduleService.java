package com.domus.schedules.services;

import com.domus.schedules.client.AcademinsApiClient;
import com.domus.schedules.dto.request.ClassGroupScheduleRequestDTO;
import com.domus.schedules.entities.ClassGroupSchedule;
import com.domus.schedules.entities.Lesson;
import com.domus.schedules.exceptions.ResourceNotFoundException;
import com.domus.schedules.repositories.ClassGroupScheduleRepository;
import com.domus.schedules.repositories.LessonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassGroupScheduleService {
    private final ModelMapper modelMapper;
    private final AcademinsApiClient academinsApiClient;
    private final ClassGroupScheduleRepository classGroupScheduleRepository;
    private final LessonRepository lessonRepository;

    public ClassGroupScheduleService(ModelMapper modelMapper, AcademinsApiClient academinsApiClient, ClassGroupScheduleRepository classGroupScheduleRepository, LessonRepository lessonRepository) {
        this.modelMapper = modelMapper;
        this.academinsApiClient = academinsApiClient;
        this.classGroupScheduleRepository = classGroupScheduleRepository;
        this.lessonRepository = lessonRepository;
    }

    public ClassGroupSchedule createClassGroupSchedule(ClassGroupScheduleRequestDTO req) {
        if (!academinsApiClient.existsClassGroup(req.getClassGroupId())) {
            throw new ResourceNotFoundException("Class Group does not exist.");
        }

        if (!academinsApiClient.existsUC(req.getCourseUnitId())) {
            throw new ResourceNotFoundException("Curricular Unit does not exist.");
        }

        if (!academinsApiClient.existsAcademicYear(req.getAcademicYearId())) {
            throw new ResourceNotFoundException("Academic year does not exist.");
        }

        ClassGroupSchedule classGroupSchedule = modelMapper.map(req, ClassGroupSchedule.class);
        return classGroupScheduleRepository.save(classGroupSchedule);
    }

    public List<Lesson> getLessonsByScheduleId(Long scheduleId) {
        ClassGroupSchedule schedule = classGroupScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Class Group Schedule not found."));

        return lessonRepository.findByClassGroupSchedule(schedule);

    }
}
