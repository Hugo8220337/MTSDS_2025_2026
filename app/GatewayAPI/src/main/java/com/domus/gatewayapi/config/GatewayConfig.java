package com.domus.gatewayapi.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${SCHEDULES_SERVICE:Schedules}")
    private String schedulesService;

    @Value("${APPLICATIONS_SERVICE:Applications}")
    private String applicationsService;

    @Value("${ACADEMINS_SERVICE:Academins}")
    private String academinsService;

    @Value("${ENROLLMENTS_SERVICE:enrollments}")
    private String enrollmentsService;

    @Value("${NOTIFICATIONS_SERVICE:Notifications}")
    private String notificationsService;

    @Value("${ASSESSMENTS_SERVICE:Assessments}")
    private String assessmentsService;

    @Value("${GRADES_REPORT_SERVICE:GradeReport}")
    private String gradesReportService;

    @Value("${ASSESSMENTS_PLANNING_SERVICE:assessments-planning}")
    private String assessmentsPlanningService;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("schedules-route", r -> r.path("/api/v1/schedules/**")
                        .uri("lb://" + schedulesService))
                .route("applications-route", r -> r.path("/api/v1/applications/**")
                        .uri("lb://" + applicationsService))
                .route("academins-route", r -> r.path("/api/v1/academins/**")
                        .uri("lb://" + academinsService))
                .route("enrollments-route", r -> r.path("/api/v1/enrollments/**")
                        .uri("lb://" + enrollmentsService))
                .route("notifications-route", r -> r.path("/api/v1/notifications/**")
                        .uri("lb://" + notificationsService))
                .route("assessments-route", r -> r.path("/api/v1/assessments/**")
                        .uri("lb://" + assessmentsService))
                .route("grade-reports-route", r -> r.path("/api/v1/grade-reports/**")
                        .uri("lb://" + gradesReportService))
                .route("assessments-planning-route", r -> r.path("/api/v1/assessments-planning/**")
                        .uri("lb://" + assessmentsPlanningService))
                .build();
    }
}