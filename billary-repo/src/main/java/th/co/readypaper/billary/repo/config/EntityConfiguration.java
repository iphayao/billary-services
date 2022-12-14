package th.co.readypaper.billary.repo.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"th.co.readypaper.billary.*"})
@EnableJpaRepositories(basePackages = {"th.co.readypaper.billary.*"})
public class EntityConfiguration {
}
