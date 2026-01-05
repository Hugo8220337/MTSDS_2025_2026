package com.domus.assessments.config;

import com.domus.assessments.dto.response.StudentResponseDTO;
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

    public List<StudentResponseDTO> getStudentsInClass(Long classGroupId) {
        if (testProfile) {
            return List.of(); // return empty list in test profile
        }

        return webClient.get()
                .uri("/admin/classes/{classGroupId}/students", classGroupId)
                .retrieve()
                .bodyToFlux(StudentResponseDTO.class)
                .collectList()
                .block();
    }

    public StudentResponseDTO getStudentById(Long studentId) {
        if (testProfile) {
            return StudentResponseDTO.builder()
                    .studentId(studentId)
                    .studentNumber("8220337")
                    .fullName("Dummy Student")
                    .fin("DUMMYFIN")
                    .build(); // return dummy student in test profile
        }

        return webClient.get()
                .uri("/admin/students/id/{studentId}", studentId)
                .retrieve()
                .bodyToFlux(StudentResponseDTO.class)
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

    public boolean existsCourse(Long courseId) {return checkExists("/admin/courses/" + courseId); }


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