package th.co.readypaper.billary.repo.entity.product;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class ProductInventoryType {
    @Id
    private int id;
    private String name;
    private String description;
}
