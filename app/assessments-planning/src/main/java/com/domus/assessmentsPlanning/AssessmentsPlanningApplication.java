package com.domus.assessmentsPlanning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class AssessmentsPlanningApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssessmentsPlanningApplication.class, args);
    }
}