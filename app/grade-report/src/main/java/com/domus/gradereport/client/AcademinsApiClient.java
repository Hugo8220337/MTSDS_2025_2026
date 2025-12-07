package com.domus.gradereport.client;

import com.domus.gradereport.dto.response.StudentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Profiles;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

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


    public List<StudentResponse> getStudentsInUcByYear(Long teacherId, String codeCU, String schoolYear) {
        if (testProfile) {
            return List.of();
        }

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/v1/academins/teachers/me/curricular-units/{codeCU}/students")
                    .queryParam("teacherId", teacherId)
                    .queryParam("schoolYear", schoolYear) 
                    .build(codeCU))
                .retrieve()
                .bodyToFlux(StudentResponse.class)
                .collectList()
                .block();
    }

    public StudentResponse getStudentById(Long studentId) {
        if (testProfile) {
            return StudentResponse.builder()
                    .studentId(studentId)
                    .studentNumber("8220337")
                    .fullName("Dummy Student")
                    .fin("DUMMYFIN")
                    .build(); // return dummy student in test profile
        }

        return webClient.get()
                .uri("/api/v1/academins/admin/students/id//id/{studentId}", studentId)
                .retrieve()
                .bodyToFlux(StudentResponse.class)
                .blockFirst();
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

    public boolean existsCourse(Long courseId) {
        return checkExists("/admin/courses/" + courseId);
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