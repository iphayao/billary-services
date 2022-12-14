package th.co.readypaper.billary.inventories.inventory.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InventoryMovementDto {
    private String id;
    private String reference;
    private int movementTypeId;
    private String inventoryId;
    private LocalDate date;
    private int quantity;
    private int remaining;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
}
