package com.userorder.config.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        // Return the current user. If using Spring Security, you can fetch the username from the security context.
        // For example:
        // return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
        return Optional.of("system"); // Replace with actual user fetching logic
    }
}
