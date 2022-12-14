package th.co.readypaper.billary.purchases;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("th.co.readypaper.billary.*")
public class BillaryServicePurchaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillaryServicePurchaseApplication.class, args);
	}

}
