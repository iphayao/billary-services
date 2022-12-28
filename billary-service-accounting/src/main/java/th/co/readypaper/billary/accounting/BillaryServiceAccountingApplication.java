package th.co.readypaper.billary.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@SpringBootApplication
@ComponentScan("th.co.readypaper.billary.*")
public class BillaryServiceAccountingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillaryServiceAccountingApplication.class, args);
	}

}
