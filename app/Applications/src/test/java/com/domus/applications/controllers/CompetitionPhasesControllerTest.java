package com.domus.applications.controllers;

import com.domus.applications.configs.ContainerizedTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.domus.applications.configs.ContainerizedTest;
import com.domus.applications.dto.request.CompetitionPhaseRequestDto;
import com.domus.applications.dto.request.DgesImportRequestDto;
import com.domus.applications.dto.response.ApplicationResponseDto;
import com.domus.applications.dto.response.CompetitionPhaseResponseDto;
import com.domus.applications.entities.Competition;
import com.domus.applications.entities.CompetitionPhase;
import com.domus.applications.repositories.ApplicationRepository;
import com.domus.applications.repositories.CompetitionPhaseRepository;
import com.domus.applications.repositories.CompetitionRepository;
import com.domus.applications.utils.TestDataFactory;
import com.domus.applications.valueObjects.ApplicationPeriod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CompetitionPhasesControllerTest extends ContainerizedTest {
    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate;
    private final CompetitionRepository competitionRepository;
    private final CompetitionPhaseRepository competitionPhaseRepository;
    private final ApplicationRepository applicationRepository;

    private String baseUrl;

    @Autowired
    public CompetitionPhasesControllerTest(
            TestRestTemplate restTemplate,
            CompetitionRepository competitionRepository,
            CompetitionPhaseRepository competitionPhaseRepository,
            ApplicationRepository applicationRepository
    ) {
        this.restTemplate = restTemplate;
        this.competitionRepository = competitionRepository;
        this.competitionPhaseRepository = competitionPhaseRepository;
        this.applicationRepository = applicationRepository;
    }

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port + "/api/v1/applications/competitions/";

        // Ordenar deletes por causa das FK
        applicationRepository.deleteAll();
        competitionPhaseRepository.deleteAll();
        competitionRepository.deleteAll();
    }

    @Test
    public void createPhaseTest() {
        Competition competition = TestDataFactory.insertCompetition(competitionRepository);

        CompetitionPhaseRequestDto request = CompetitionPhaseRequestDto.builder()
                .phaseNumber(1)
                .totalVacancies(30)
                .period(new ApplicationPeriod(
                        LocalDate.now(),
                        LocalDate.now().plusDays(10)
                ))
                .build();

        ResponseEntity<CompetitionPhaseResponseDto> response = restTemplate.postForEntity(
                baseUrl + competition.getId() + "/phases",
                request,
                CompetitionPhaseResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPhaseNumber()).isEqualTo(1);
        assertThat(response.getBody().getTotalVacancies()).isEqualTo(30);
    }

    @Test
    public void listPhasesTest() {
        Competition competition = TestDataFactory.insertCompetition(competitionRepository);

        CompetitionPhase phase1 = TestDataFactory.insertCompetitionPhase(competitionPhaseRepository, competition);
        CompetitionPhase phase2 = TestDataFactory.insertCompetitionPhase(competitionPhaseRepository, competition);

        ResponseEntity<CompetitionPhaseResponseDto[]> response = restTemplate.getForEntity(
                baseUrl + competition.getId() + "/phases",
                CompetitionPhaseResponseDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(2);
    }

    @Test
    public void importDgesTest() {
        Competition competition = TestDataFactory.insertCompetition(competitionRepository);
        CompetitionPhase phase = TestDataFactory.insertCompetitionPhase(competitionPhaseRepository, competition);

        DgesImportRequestDto request = new DgesImportRequestDto(
                "2024/2025",
                List.of(
                        new DgesImportRequestDto.DgesPlacementDto("Alice Silva", "123456789", 1L),
                        new DgesImportRequestDto.DgesPlacementDto("Bruno Costa", "987654321", 2L)
                )
        );

        HttpEntity<DgesImportRequestDto> entity = new HttpEntity<>(request);

        ResponseEntity<ApplicationResponseDto[]> response = restTemplate.exchange(
                baseUrl + competition.getId() + "/phases/" + phase.getId() + "/dges-import",
                HttpMethod.POST,
                entity,
                ApplicationResponseDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(2); // 2 NIFs importados
    }
}
