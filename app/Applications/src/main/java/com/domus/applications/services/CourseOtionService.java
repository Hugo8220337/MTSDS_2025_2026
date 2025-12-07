package com.domus.applications.services;

import com.domus.applications.client.AcademinsApiClient;
import com.domus.applications.dto.request.CourseOptionRequestDto;
import com.domus.applications.dto.request.UpdateStatusRequestDto;
import com.domus.applications.entities.Application;
import com.domus.applications.entities.CourseOption;
import com.domus.applications.repositories.CourseOptionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseOtionService {
    private final ModelMapper mapper;
    private final AcademinsApiClient academinsApiClient;
    private final ApplicationService applicationService;
    private final CourseOptionRepository courseOptionRepository;

    public CourseOtionService(ModelMapper mapper, AcademinsApiClient academinsApiClient, ApplicationService applicationService, CourseOptionRepository courseOptionRepository) {
        this.mapper = mapper;
        this.academinsApiClient = academinsApiClient;
        this.applicationService = applicationService;
        this.courseOptionRepository = courseOptionRepository;
    }

    public CourseOption addCourseOption(Long applicationId, CourseOptionRequestDto courseOptionRequestDto) {
        Application application = applicationService.getApplicationById(applicationId);

        if (!academinsApiClient.existsCourse(courseOptionRequestDto.getCourseId())) {
            throw new EntityNotFoundException("Assigned responible teacher does not exist.");
        }

        CourseOption courseOption = mapper.map(courseOptionRequestDto, CourseOption.class);
        courseOption.setApplication(application);
        return courseOptionRepository.save(courseOption);
    }

    public List<CourseOption> getCourseOptionsByApplicationId(Long applicationId) {
        Application application = applicationService.getApplicationById(applicationId);
        return courseOptionRepository.findByApplication(application);
    }

    public void removeCourseOption(Long applicationId, Long courseOptionId) {
        Application application = applicationService.getApplicationById(applicationId);
        CourseOption courseOption = courseOptionRepository.findByIdAndApplication(courseOptionId, application)
                .orElseThrow(() -> new EntityNotFoundException("Course option not found for the given application."));
        courseOptionRepository.delete(courseOption);
    }

    public CourseOption updateCourseOption(Long applicationId, Long courseOptionId, UpdateStatusRequestDto newStatus) {
        Application application = applicationService.getApplicationById(applicationId);
        CourseOption courseOption = courseOptionRepository.findByIdAndApplication(courseOptionId, application)
                .orElseThrow(() -> new EntityNotFoundException("Course option not found for the given application."));
        courseOption.setStatus(newStatus.getStatus());
        return courseOptionRepository.save(courseOption);
    }
}
