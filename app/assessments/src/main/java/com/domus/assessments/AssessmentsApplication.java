package com.domus.assessments;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@EnableMongoAuditing
public class AssessmentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssessmentsApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}