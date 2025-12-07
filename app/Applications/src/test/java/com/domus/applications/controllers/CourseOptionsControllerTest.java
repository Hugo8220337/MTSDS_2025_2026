package com.domus.applications.controllers;

import com.domus.applications.configs.ContainerizedTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.domus.applications.dto.request.CourseOptionRequestDto;
import com.domus.applications.dto.request.UpdateStatusRequestDto;
import com.domus.applications.dto.response.CourseOptionResponseDto;
import com.domus.applications.entities.Application;
import com.domus.applications.entities.Competition;
import com.domus.applications.entities.CompetitionPhase;
import com.domus.applications.entities.CourseOption;
import com.domus.applications.repositories.ApplicationRepository;
import com.domus.applications.repositories.CompetitionPhaseRepository;
import com.domus.applications.repositories.CompetitionRepository;
import com.domus.applications.repositories.CourseOptionRepository;
import com.domus.applications.utils.TestDataFactory;
import com.domus.applications.valueObjects.ApplicantInfo;
import com.domus.applications.valueObjects.CourseOptionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CourseOptionsControllerTest extends ContainerizedTest {
    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate;

    private final CompetitionRepository competitionRepository;
    private final CompetitionPhaseRepository competitionPhaseRepository;
    private final ApplicationRepository applicationRepository;
    private final CourseOptionRepository courseOptionRepository;

    private String baseUrl;

    @Autowired
    public CourseOptionsControllerTest(
            TestRestTemplate restTemplate,
            CompetitionRepository competitionRepository,
            CompetitionPhaseRepository competitionPhaseRepository,
            ApplicationRepository applicationRepository,
            CourseOptionRepository courseOptionRepository
    ) {
        this.restTemplate = restTemplate;
        this.competitionRepository = competitionRepository;
        this.competitionPhaseRepository = competitionPhaseRepository;
        this.applicationRepository = applicationRepository;
        this.courseOptionRepository = courseOptionRepository;
    }

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port + "/api/v1/applications/";

        // Clean DB in correct FK order
        competitionPhaseRepository.deleteAll();
        competitionRepository.deleteAll();
        courseOptionRepository.deleteAll();
        applicationRepository.deleteAll();
    }

    private Application createDraftApplication() {
        Competition competition = TestDataFactory.insertCompetition(competitionRepository);
        CompetitionPhase phase = TestDataFactory.insertCompetitionPhase(competitionPhaseRepository, competition);

        Application application = new Application(
                phase.getId(),
                new ApplicantInfo("100200300", "Test User")
        );

        return applicationRepository.save(application);
    }

    @Test
    public void addCourseOptionTest() {
        Application app = createDraftApplication();

        CourseOptionRequestDto request = CourseOptionRequestDto.builder()
                .courseId(99L)
                .preferenceOrder(1)
                .build();

        ResponseEntity<CourseOptionResponseDto> response = restTemplate.postForEntity(
                baseUrl + app.getId() + "/add-course-option",
                request,
                CourseOptionResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCourseId()).isEqualTo(99L);
        assertThat(response.getBody().getPreferenceOrder()).isEqualTo(1);
    }

    @Test
    public void getCourseOptionsTest() {
        Application app = createDraftApplication();
        CourseOption option = TestDataFactory.insertCourseOption(courseOptionRepository, app);

        ResponseEntity<CourseOptionResponseDto[]> response = restTemplate.getForEntity(
                baseUrl + app.getId() + "/course-options",
                CourseOptionResponseDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(1);
        assertThat(response.getBody()[0].getId()).isEqualTo(option.getId());
    }

    @Test
    public void removeCourseOptionTest() {
        Application app = createDraftApplication();
        CourseOption option = TestDataFactory.insertCourseOption(courseOptionRepository, app);

        restTemplate.delete(
                baseUrl + app.getId() + "/course-options/" + option.getId() + "/remove"
        );

        assertThat(courseOptionRepository.findById(option.getId())).isEmpty();
    }

    @Test
    public void updateCourseStatusTest() {
        Application app = createDraftApplication();
        CourseOption option = TestDataFactory.insertCourseOption(courseOptionRepository, app);

        UpdateStatusRequestDto dto = new UpdateStatusRequestDto(CourseOptionStatus.APPROVED);

        ResponseEntity<CourseOptionResponseDto> response = restTemplate.exchange(
                baseUrl + app.getId() + "/course-options/" + option.getId() + "/update-status",
                HttpMethod.PUT,
                new HttpEntity<>(dto),
                CourseOptionResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(CourseOptionStatus.APPROVED);
    }
}
