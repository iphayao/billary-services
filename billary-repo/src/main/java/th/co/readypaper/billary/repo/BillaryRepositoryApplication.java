package th.co.readypaper.billary.repo;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"th.co.readypaper.billary.*"})
@EnableJpaRepositories(basePackages = {"th.co.readypaper.billary.*"})
public class BillaryRepositoryApplication {

	public static void main(String[] args) {

	}

}
