package com.domus.schedules.controllers;

import com.domus.schedules.configs.ContainerizedTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AdminScheduleControllerTest extends ContainerizedTest {
}
