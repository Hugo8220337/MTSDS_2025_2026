package com.domus.applications.controllers;

import com.domus.applications.configs.ContainerizedTest;
import com.domus.applications.dto.request.SubmitApplicationRequestDto;
import com.domus.applications.dto.request.UpdateApplicationStatusRequestDto;
import com.domus.applications.dto.response.ApplicationResponseDto;
import com.domus.applications.entities.Application;
import com.domus.applications.entities.Competition;
import com.domus.applications.entities.CompetitionPhase;
import com.domus.applications.entities.CourseOption;
import com.domus.applications.repositories.CompetitionPhaseRepository;
import com.domus.applications.repositories.ApplicationRepository;
import com.domus.applications.repositories.CompetitionRepository;
import com.domus.applications.repositories.CourseOptionRepository;
import com.domus.applications.utils.TestDataFactory;
import com.domus.applications.valueObjects.ApplicantInfo;
import com.domus.applications.valueObjects.ApplicationPeriod;
import com.domus.applications.valueObjects.ApplicationProcessType;
import com.domus.applications.valueObjects.ApplicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.server.LocalServerPort;


import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApplicationsControllerTest extends ContainerizedTest {
    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate;

    private final CompetitionPhaseRepository competitionPhaseRepository;

    private final CompetitionRepository competitionRepository;

    private final ApplicationRepository applicationRepository;

    private final CourseOptionRepository courseOptionRepository;

    private String baseUrl;

    @Autowired
    public ApplicationsControllerTest(TestRestTemplate restTemplate, CompetitionPhaseRepository competitionPhaseRepository, CompetitionRepository competitionRepository, ApplicationRepository applicationRepository, CourseOptionRepository courseOptionRepository) {
        this.restTemplate = restTemplate;
        this.competitionPhaseRepository = competitionPhaseRepository;
        this.competitionRepository = competitionRepository;
        this.applicationRepository = applicationRepository;
        this.courseOptionRepository = courseOptionRepository;
    }

    @BeforeEach
    void setup() {
        // Configure base URL with random port
        baseUrl = "http://localhost:" + port + "/api/v1/applications/";

        // Clean up repositories before each test (TEM DE SEGUIR ESTA ORDEM POR CAUSA DAS FK)
        competitionPhaseRepository.deleteAll();
        competitionRepository.deleteAll();
        courseOptionRepository.deleteAll();
        applicationRepository.deleteAll();
    }

    @Test
    public void createDraftTest() {
        Competition competition = TestDataFactory.insertCompetition(competitionRepository);
        CompetitionPhase phase = TestDataFactory.insertCompetitionPhase(competitionPhaseRepository, competition);

        SubmitApplicationRequestDto draftApplication = SubmitApplicationRequestDto.builder()
                .phaseId(phase.getId())
                .applicantInfo(new ApplicantInfo("123456789", "John Doe"))
                .build();

        ResponseEntity<Application> response = restTemplate.postForEntity(
                baseUrl + "create-draft",
                draftApplication,
                Application.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getApplicantInfo().getNif()).isEqualTo("123456789");
        assertThat(response.getBody().getApplicantInfo().getFullName()).isEqualTo("John Doe");
    }

    @Test
    public void submitApplicationTest() {
        Competition competition = TestDataFactory.insertCompetition(competitionRepository);
        CompetitionPhase phase = TestDataFactory.insertCompetitionPhase(competitionPhaseRepository, competition);

        SubmitApplicationRequestDto draftApplication = SubmitApplicationRequestDto.builder()
                .phaseId(phase.getId())
                .applicantInfo(new ApplicantInfo("987654321", "Jane Smith"))
                .build();

        // First, create a draft application
        ResponseEntity<Application> draftResponse = restTemplate.postForEntity(
                baseUrl + "create-draft",
                draftApplication,
                Application.class
        );

        Long applicationId = draftResponse.getBody().getId();

        // Cannot submit without course options
        TestDataFactory.insertCourseOption(courseOptionRepository, draftResponse.getBody());

        // Now, submit the application
        ResponseEntity<Application> submitResponse = restTemplate.postForEntity(
                baseUrl + applicationId + "/submit",
                null,
                Application.class
        );

        assertThat(submitResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(submitResponse.getBody()).isNotNull();
        assertThat(submitResponse.getBody().getId()).isEqualTo(applicationId);
    }

    @Test
    public void getApplicationTest() {
        Competition competition = TestDataFactory.insertCompetition(competitionRepository);
        CompetitionPhase phase = TestDataFactory.insertCompetitionPhase(competitionPhaseRepository, competition);

        SubmitApplicationRequestDto draftApplication = SubmitApplicationRequestDto.builder()
                .phaseId(phase.getId())
                .applicantInfo(new ApplicantInfo("555555555", "Alice Johnson"))
                .build();

        // Create a draft application
        ResponseEntity<Application> draftResponse = restTemplate.postForEntity(
                baseUrl + "create-draft",
                draftApplication,
                Application.class
        );

        Long applicationId = draftResponse.getBody().getId();

        // Retrieve the application
        ResponseEntity<Application> getResponse = restTemplate.getForEntity(
                baseUrl + applicationId,
                Application.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getApplicantInfo().getNif()).isEqualTo("555555555");
        assertThat(getResponse.getBody().getApplicantInfo().getFullName()).isEqualTo("Alice Johnson");
    }

    @Test
    public void updateStateTest() {
        Competition competition = TestDataFactory.insertCompetition(competitionRepository);
        CompetitionPhase phase = TestDataFactory.insertCompetitionPhase(competitionPhaseRepository, competition);

        SubmitApplicationRequestDto draftApplication = SubmitApplicationRequestDto.builder()
                .phaseId(phase.getId())
                .applicantInfo(new ApplicantInfo("222333444", "Bob Brown"))
                .build();

        // Create a draft application
        ResponseEntity<ApplicationResponseDto> draftResponse = restTemplate.postForEntity(
                baseUrl + "create-draft",
                draftApplication,
                ApplicationResponseDto.class
        );

        Long applicationId = draftResponse.getBody().getId();

        // Update application state
        ResponseEntity<ApplicationResponseDto> updateResponse = restTemplate.exchange(
                baseUrl + applicationId + "/update-state",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateApplicationStatusRequestDto(ApplicationStatus.SUBMITTED)),
                ApplicationResponseDto.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isNotNull();
        assertThat(updateResponse.getBody().getStatus().toString()).isEqualTo("SUBMITTED");
    }

    @Test
    public void updateDraftTest() {
        Competition competition = TestDataFactory.insertCompetition(competitionRepository);
        CompetitionPhase phase = TestDataFactory.insertCompetitionPhase(competitionPhaseRepository, competition);

        SubmitApplicationRequestDto draftApplication = SubmitApplicationRequestDto.builder()
                .phaseId(phase.getId())
                .applicantInfo(new ApplicantInfo("666777888", "Charlie Davis"))
                .build();

        // Create a draft application
        ResponseEntity<Application> draftResponse = restTemplate.postForEntity(
                baseUrl + "create-draft",
                draftApplication,
                Application.class
        );

        Long applicationId = draftResponse.getBody().getId();

        // Update draft application
        SubmitApplicationRequestDto updatedDraft = SubmitApplicationRequestDto.builder()
                .phaseId(phase.getId())
                .applicantInfo(new ApplicantInfo("666777888", "Charlie D."))
                .build();

        restTemplate.put(
                baseUrl + applicationId + "/update-draft",
                updatedDraft
        );

        // Retrieve the updated application
        ResponseEntity<Application> getResponse = restTemplate.getForEntity(
                baseUrl + applicationId,
                Application.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getApplicantInfo().getFullName()).isEqualTo("Charlie D.");
    }
}
