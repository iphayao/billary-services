package th.co.readypaper.billary.repo.entity.inventory;

import lombok.*;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InventoryMovement extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String reference;
    private int movementTypeId;
    @Column(name = "inventory_id")
    private UUID inventoryId;
    private LocalDate date;
    private int quantity;
    private int remaining;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
}
