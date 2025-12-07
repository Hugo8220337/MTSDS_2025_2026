package com.domus.schedules;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SchedulesApplication.class)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScheduleIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        if (!postgres.isRunning()){
            postgres.start();
        }
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @BeforeEach
    void setUp() {
        // Se necessário limpar tabelas:
        // jdbcTemplate.execute("TRUNCATE table schedules RESTART IDENTITY CASCADE");
    }

    @SneakyThrows
    @Test
    void createAndGetSchedule() {
        var request = new CreateScheduleRequest("2025-12-01T10:00", "Meeting with team");
        String url="http://localhost:" + port + "/api/schedules";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<CreateScheduleRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> respStr=restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        assertThat(respStr.getStatusCode().is2xxSuccessful())
                .withFailMessage(() -> "POST to " + url + " failed : status = " + respStr.getStatusCode()+" body = " + respStr.getBody())
                .isTrue();

        ObjectMapper mapper = new ObjectMapper();
        ScheduleDto created = mapper.readValue(respStr.getBody(), ScheduleDto.class);
        assertThat(created).isNotNull();
        assertThat(created.id()).isNotNull();

        // GET
        String getURL = "http://localhost:" + port + "/api/schedules/" + created.id();
        ResponseEntity<ScheduleDto> getRespStr = restTemplate.getForEntity(getURL, ScheduleDto.class);

        assertThat(getRespStr.getStatusCode().is2xxSuccessful())
                .withFailMessage(() -> "GET " + getURL + " failed : status = " + getRespStr.getStatusCode() + "body=" + getRespStr.getBody())
                        .isTrue();

        ScheduleDto fetched = getRespStr.getBody();
        assertThat(fetched.title()).isEqualTo("Meeting with team");
    }

    // DTOs simples para o teste (evitam precisar de classes externas)
    static record CreateScheduleRequest(String when, String title) {}
    static record ScheduleDto(Long id, String when, String title) {}
}















