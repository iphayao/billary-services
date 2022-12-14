package th.co.readypaper.billary.ompanies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("th.co.readypaper.billary.*")
public class BillaryServiceCompanyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillaryServiceCompanyApplication.class, args);
	}

}
