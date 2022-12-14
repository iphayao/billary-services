package th.co.readypaper.billary.repo.entity.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Product extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    @OneToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private ProductType type;
    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private ProductCategory category;
    @OneToOne
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    private ProductUnit unit;
    @OneToOne
    @JoinColumn(name = "vat_type_id", referencedColumnName = "id")
    private ProductVatType vatType;
    @OneToOne
    @JoinColumn(name = "inventory_type_id", referencedColumnName = "id")
    private ProductInventoryType inventoryType;
}
