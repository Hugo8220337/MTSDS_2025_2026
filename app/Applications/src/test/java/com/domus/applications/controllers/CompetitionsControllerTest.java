package com.domus.applications.controllers;

import com.domus.applications.configs.ContainerizedTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.domus.applications.dto.request.CompetitionRequestDto;
import com.domus.applications.dto.response.CompetitionResponseDto;
import com.domus.applications.entities.Competition;
import com.domus.applications.repositories.CompetitionRepository;
import com.domus.applications.utils.TestDataFactory;
import com.domus.applications.valueObjects.ApplicationPeriod;
import com.domus.applications.valueObjects.ApplicationProcessStatus;
import com.domus.applications.valueObjects.ApplicationProcessType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.http.*;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CompetitionsControllerTest extends ContainerizedTest {
    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate;
    private final CompetitionRepository competitionRepository;

    private String baseUrl;

    @Autowired
    public CompetitionsControllerTest(
            TestRestTemplate restTemplate,
            CompetitionRepository competitionRepository
    ) {
        this.restTemplate = restTemplate;
        this.competitionRepository = competitionRepository;
    }

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port + "/api/v1/applications/competitions";

        competitionRepository.deleteAll();
    }

    @Test
    void createCompetitionTest() {
        CompetitionRequestDto request = CompetitionRequestDto.builder()
                .name("CNA 2025")
                .type(ApplicationProcessType.CTESP)
                .academicYear("2024/2025")
                .status(ApplicationProcessStatus.OPEN)
                .period(new ApplicationPeriod(
                        LocalDate.now(),
                        LocalDate.now().plusDays(30)
                ))
                .build();

        ResponseEntity<CompetitionResponseDto> response = restTemplate.postForEntity(
                baseUrl,
                request,
                CompetitionResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("CNA 2025");
    }

    @Test
    void listCompetitionsTest() {
        TestDataFactory.insertCompetition(competitionRepository);
        TestDataFactory.insertCompetition(competitionRepository);

        ResponseEntity<CompetitionResponseDto[]> response = restTemplate.getForEntity(
                baseUrl,
                CompetitionResponseDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(2);
    }

    @Test
    void getCompetitionByIdTest() {
        Competition competition = TestDataFactory.insertCompetition(competitionRepository);

        ResponseEntity<CompetitionResponseDto> response = restTemplate.getForEntity(
                baseUrl + "/" + competition.getId(),
                CompetitionResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(competition.getId());
        assertThat(response.getBody().getName()).isEqualTo(competition.getName());
    }

    @Test
    void updateCompetitionTest() {
        Competition competition = TestDataFactory.insertCompetition(competitionRepository);

        CompetitionRequestDto updateRequest = CompetitionRequestDto.builder()
                .name("CNA Updated")
                .type(ApplicationProcessType.CTESP)
                .academicYear("2024/2025")
                .status(ApplicationProcessStatus.CLOSED)
                .period(new ApplicationPeriod(
                        LocalDate.now(),
                        LocalDate.now().plusDays(60)
                ))
                .build();

        HttpEntity<CompetitionRequestDto> entity = new HttpEntity<>(updateRequest);

        ResponseEntity<CompetitionResponseDto> response = restTemplate.exchange(
                baseUrl + "/" + competition.getId() + "/update",
                HttpMethod.PUT,
                entity,
                CompetitionResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("CNA Updated");
        assertThat(response.getBody().getStatus()).isEqualTo(ApplicationProcessStatus.CLOSED);
    }
}
