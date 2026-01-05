package com.domus.schedules.controllers;

import com.domus.schedules.configs.ContainerizedTest;
import com.domus.schedules.dto.response.EquipmentResponseDTO;
import com.domus.schedules.entities.Equipment;
import com.domus.schedules.repositories.EquipmentRepository;
import com.domus.schedules.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AdminEquipmentControllerTest extends ContainerizedTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate;
    private final EquipmentRepository equipmentRepository;

    private String baseUrl;

    @Autowired
    public AdminEquipmentControllerTest(TestRestTemplate restTemplate,
                                        EquipmentRepository equipmentRepository) {
        this.restTemplate = restTemplate;
        this.equipmentRepository = equipmentRepository;
    }

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port + "/api/v1/schedules/admin/";
        // Clean repository between tests
        equipmentRepository.deleteAll();
    }

    @Test
    void getAllEquipment_whenThereIsNoEquipment_thenReturnEmptyList() {
        ResponseEntity<EquipmentResponseDTO[]> response = restTemplate.getForEntity(baseUrl + "equipment", EquipmentResponseDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        EquipmentResponseDTO[] body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).isEmpty();
    }

    @Test
    void getAllEquipment_whenThereAreEquipments_thenReturnList() {
        // Insert sample equipments
        Equipment e1 = TestDataFactory.insertEquipment(equipmentRepository);
        Equipment e2 = TestDataFactory.insertEquipment(equipmentRepository);

        ResponseEntity<EquipmentResponseDTO[]> response = restTemplate.getForEntity(baseUrl + "equipment", EquipmentResponseDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        EquipmentResponseDTO[] body = response.getBody();
        assertThat(body).isNotNull();

        List<EquipmentResponseDTO> equipments = Arrays.asList(body);
        // We inserted two equipments (TestDataFactory.insertEquipment uses constant values)
        assertThat(equipments.size()).isEqualTo(2);

        // Basic field checks on the first element
        EquipmentResponseDTO first = equipments.get(0);
        assertThat(first.getId()).isNotNull();
        assertThat(first.getName()).isEqualTo(e1.getName());
        assertThat(first.getDescription()).isEqualTo(e1.getDescription());
    }
}
