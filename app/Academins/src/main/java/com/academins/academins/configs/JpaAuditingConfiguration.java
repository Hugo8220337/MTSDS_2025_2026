package com.academins.academins.configs;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class to enable JPA Auditing in the application.
 * This class is annotated with @Configuration and @EnableJpaAuditing
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {
}
