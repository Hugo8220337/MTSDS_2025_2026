package com.domus.assessmentsPlanning.client;

import com.domus.assessmentsPlanning.dto.external.ExternalDTOs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.core.env.Environment;

@Service
public class AcademinsApiClient {

    private final WebClient webClient;
    private final boolean testProfile;

    public AcademinsApiClient(@Value("${services.academins.url}") String baseUrl, Environment environment) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.testProfile = environment.acceptsProfiles(Profiles.of("test"));
    }

    public boolean existsSchool(Long schoolId) {
        return checkExists("/schools/id/" + schoolId);
    }

    public boolean existsClassGroup(Long classGroupId) {
        return checkExists("/classes/" + classGroupId);
    }

    public boolean existsTeacher(Long teacherId) {
        return checkExists("/teachers/" + teacherId);
    }

    public boolean existsUC(Long ucId) {
        return checkExists("/admin/curricular-units/" + ucId);
    }

    public boolean existsAcademicYear(Long academicYearId) {
        return checkExists("/schoolyear/" + academicYearId);
    }

    public boolean existsCourse(Long courseId) {return checkExists("/admin/courses/" + courseId); }

    public ExternalDTOs.CourseUnitDTO getCourseById(Long courseId) {
        if (testProfile) {
            return new ExternalDTOs.CourseUnitDTO(); // return empty DTO in test profile
        }

        try {
            return webClient.get()
                    .uri("/admin/courses/" + courseId)
                    .retrieve()
                    .bodyToMono(ExternalDTOs.CourseUnitDTO.class)
                    .block();

        } catch (WebClientResponseException.NotFound e) {
            return null; // or throw an exception based on your error handling strategy
        }
    }

    public ExternalDTOs.ProfessorDTO getProfessorById(Long professorId) {
        if (testProfile) {
            return new ExternalDTOs.ProfessorDTO(); // return empty DTO in test profile
        }

        try {
            return webClient.get()
                    .uri("/teachers/" + professorId)
                    .retrieve()
                    .bodyToMono(ExternalDTOs.ProfessorDTO.class)
                    .block();

        } catch (WebClientResponseException.NotFound e) {
            return null; // or throw an exception based on your error handling strategy
        }
    }

    private boolean checkExists(String path) {
        if (testProfile) {
            return true; // always true in test profile
        }

        try {
            webClient.get()
                    .uri(path)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            return true; // 200 OK = Exists

        } catch (WebClientResponseException.NotFound e) {
            return false; // 404 = Don't Exist
        }
    }
}