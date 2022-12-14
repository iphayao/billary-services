package th.co.readypaper.billary.sales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("th.co.readypaper.billary.*")
public class BillaryServiceSalesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillaryServiceSalesApplication.class, args);
    }

}
