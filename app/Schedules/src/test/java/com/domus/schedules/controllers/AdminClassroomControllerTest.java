package com.domus.schedules.controllers;

import com.domus.schedules.configs.ContainerizedTest;
import com.domus.schedules.dto.ClassroomLocationDTO;
import com.domus.schedules.dto.request.ClassroomEquipmentRequestDTO;
import com.domus.schedules.dto.request.ClassroomRequestDTO;
import com.domus.schedules.entities.Classroom;
import com.domus.schedules.entities.ClassroomEquipment;
import com.domus.schedules.entities.Equipment;
import com.domus.schedules.repositories.ClassroomEquipmentRepository;
import com.domus.schedules.repositories.ClassroomRepository;
import com.domus.schedules.repositories.EquipmentRepository;
import com.domus.schedules.utils.TestDataFactory;
import com.domus.schedules.valueObjects.ClassroomLocation;
import com.domus.schedules.valueObjects.ClassroomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AdminClassroomControllerTest extends ContainerizedTest {
    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate;

    private final ClassroomRepository classroomRepository;

    private final EquipmentRepository equipmentRepository;

    private final ClassroomEquipmentRepository classroomEquipmentRepository;

    private String baseUrl;

    @Autowired
    public AdminClassroomControllerTest(TestRestTemplate restTemplate,
                                        ClassroomRepository classroomRepository,
                                        EquipmentRepository equipmentRepository,
                                        ClassroomEquipmentRepository classroomEquipmentRepository) {
        this.restTemplate = restTemplate;
        this.classroomRepository = classroomRepository;
        this.equipmentRepository = equipmentRepository;
        this.classroomEquipmentRepository = classroomEquipmentRepository;
    }

    @BeforeEach
    void setup() {
        // Configure base URL with random port
        baseUrl = "http://localhost:" + port + "/api/v1/schedules/admin/";

        // Clean up repositories before each test (TEM DE SEGUIR ESTA ORDEM POR CAUSA DAS FK)
        classroomEquipmentRepository.deleteAll();
        equipmentRepository.deleteAll();
        classroomRepository.deleteAll();
    }

    @Test
    public void createClassroomTest() {
        ClassroomRequestDTO classroomToInsert = ClassroomRequestDTO.builder()
                .type(ClassroomType.AUDITORIUM)
                .capacity(30)
                .schoolId(1L)
                .location(new ClassroomLocationDTO("Building A", 1, "Room 101"))
                .build();

        ResponseEntity<Classroom> response = restTemplate.postForEntity(baseUrl + "schools/1/create-classroom", classroomToInsert, Classroom.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void getClassroomAvailabilityTest() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "classrooms/1/availability?startDate=2024-07-01", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void addEquipmentToClassroomTest() {
        Classroom classroom = TestDataFactory.insertClassroom(classroomRepository);
        Equipment equipment = TestDataFactory.insertEquipment(equipmentRepository);

        ClassroomEquipmentRequestDTO request = ClassroomEquipmentRequestDTO.builder()
                .equipmentId(equipment.getId())
                .quantity(5)
                .observations("(╯'□')╯︵ ┻━┻")
                .build();

        String url = baseUrl + "classrooms/" + classroom.getId() + "/equipment";
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void removeEquipmentFromClassroomTest() {
        Classroom classroom = TestDataFactory.insertClassroom(classroomRepository);
        Equipment equipment = TestDataFactory.insertEquipment(equipmentRepository);
        ClassroomEquipment ce = TestDataFactory.insertClassroomEquipment(
                classroomEquipmentRepository,
                classroomRepository,
                equipmentRepository,
                equipment.getId(),
                classroom.getId()
        );

        String url = baseUrl + "classrooms/" + classroom.getId() + "/equipments/" + equipment.getId();
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void getClassroomEquipmentTest() {
        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl + "classrooms/1/equipment", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}
