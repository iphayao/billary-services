package th.co.readypaper.billary.repo.entity.product;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class ProductType {
    @Id
    private int id;
    private String name;
    private String description;
}
