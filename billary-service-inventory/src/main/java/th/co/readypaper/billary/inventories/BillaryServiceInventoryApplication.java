package th.co.readypaper.billary.inventories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("th.co.readypaper.billary.*")
public class BillaryServiceInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillaryServiceInventoryApplication.class, args);
	}

}
