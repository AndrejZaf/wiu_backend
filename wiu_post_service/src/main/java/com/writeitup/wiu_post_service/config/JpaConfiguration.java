package com.writeitup.wiu_post_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

import static com.writeitup.wiu_post_service.util.JwtUtil.getJwtClaim;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfiguration {

    @Bean
    public AuditorAware<UUID> auditorAware() {
        return new AuditorAwareImpl();
    }

    static class AuditorAwareImpl implements AuditorAware<UUID> {

        @Override
        public Optional<UUID> getCurrentAuditor() {
            return Optional.of(UUID.fromString(getJwtClaim("sub")));
        }
    }
}
