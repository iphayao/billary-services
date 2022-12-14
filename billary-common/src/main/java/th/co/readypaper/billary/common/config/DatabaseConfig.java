package th.co.readypaper.billary.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class DatabaseConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            return Optional.of(jwt.getClaim("preferred_username"));
        };
    }



}
