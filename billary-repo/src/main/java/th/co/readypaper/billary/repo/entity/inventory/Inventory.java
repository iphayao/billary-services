package th.co.readypaper.billary.repo.entity.inventory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;
import th.co.readypaper.billary.repo.entity.product.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Inventory extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;
    private int locationId;
    private int currentQty;
    private BigDecimal currentPrice;
    private int initialQty;
    private BigDecimal initialPrice;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "inventory_id")
    private List<InventoryMovement> movements;
}
