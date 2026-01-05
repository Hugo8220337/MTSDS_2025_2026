package com.domus.assessmentsPlanning.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class to enable JPA Auditing features.
 * This allows automatic population of auditing fields such as createdDate, lastModifiedDate, etc.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {

}

