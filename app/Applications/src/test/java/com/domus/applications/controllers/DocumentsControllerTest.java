package com.domus.applications.controllers;

import com.domus.applications.configs.ContainerizedTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.domus.applications.configs.ContainerizedTest;
import com.domus.applications.dto.response.DocumentResponseDto;
import com.domus.applications.entities.Application;
import com.domus.applications.entities.ApplicationDocument;
import com.domus.applications.entities.Competition;
import com.domus.applications.entities.CompetitionPhase;
import com.domus.applications.repositories.*;
import com.domus.applications.utils.TestDataFactory;
import com.domus.applications.valueObjects.ApplicantInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DocumentsControllerTest extends ContainerizedTest {
    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate;

    private final CompetitionRepository competitionRepository;
    private final CompetitionPhaseRepository competitionPhaseRepository;
    private final ApplicationRepository applicationRepository;
    private final DocumentRepository documentRepository;

    private String baseUrl;

    @Autowired
    public DocumentsControllerTest(
            TestRestTemplate restTemplate,
            CompetitionRepository competitionRepository,
            CompetitionPhaseRepository competitionPhaseRepository,
            ApplicationRepository applicationRepository,
            DocumentRepository documentRepository
    ) {
        this.restTemplate = restTemplate;
        this.competitionRepository = competitionRepository;
        this.competitionPhaseRepository = competitionPhaseRepository;
        this.applicationRepository = applicationRepository;
        this.documentRepository = documentRepository;
    }

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port + "/api/v1/applications/";

        // FK cleanup order
        competitionPhaseRepository.deleteAll();
        competitionRepository.deleteAll();
        documentRepository.deleteAll();
        applicationRepository.deleteAll();
    }

    private Application createDraftApplication() {
        Competition competition = TestDataFactory.insertCompetition(competitionRepository);
        CompetitionPhase phase = TestDataFactory.insertCompetitionPhase(competitionPhaseRepository, competition);

        Application application = new Application(
                phase.getId(),
                new ApplicantInfo("111222333", "Doc Tester")
        );

        return applicationRepository.save(application);
    }

    @Test
    void uploadDocumentTest() {
        Application app = createDraftApplication();

        byte[] fileContent = "test file content".getBytes();
        ByteArrayResource resource = new ByteArrayResource(fileContent) {
            @Override public String getFilename() { return "testfile.pdf"; }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new org.springframework.util.LinkedMultiValueMap<>();
        body.add("documentType", "NIF");
        body.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<DocumentResponseDto> response = restTemplate.exchange(
                baseUrl + app.getId() + "/documents",
                HttpMethod.POST,
                requestEntity,
                DocumentResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDocumentType()).isEqualTo("NIF");
        assertThat(response.getBody().getDocumentBase64()).isNotEmpty();
    }

    @Test
    void listDocumentsTest() {
        Application app = createDraftApplication();

        // Insert a document manually via repository
        ApplicationDocument doc = new ApplicationDocument(app, "NIF", "YmFzZTY0");
        documentRepository.save(doc);

        ResponseEntity<DocumentResponseDto[]> response = restTemplate.getForEntity(
                baseUrl + app.getId() + "/documents",
                DocumentResponseDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(1);
        assertThat(response.getBody()[0].getDocumentType()).isEqualTo("NIF");
    }

    @Test
    void getDocumentTest() {
        Application app = createDraftApplication();

        ApplicationDocument doc = new ApplicationDocument(app, "ID_CARD", "ZXhhbXBsZQ==");
        documentRepository.save(doc);

        ResponseEntity<DocumentResponseDto> response = restTemplate.getForEntity(
                baseUrl + app.getId() + "/documents/" + doc.getId(),
                DocumentResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(doc.getId());
        assertThat(response.getBody().getDocumentType()).isEqualTo("ID_CARD");
    }

    @Test
    void deleteDocumentTest() {
        Application app = createDraftApplication();

        ApplicationDocument doc = new ApplicationDocument(app, "PHOTO", "cGhvdG8=");
        documentRepository.save(doc);

        restTemplate.delete(baseUrl + app.getId() + "/documents/" + doc.getId());

        assertThat(documentRepository.findById(doc.getId())).isEmpty();
    }
}
