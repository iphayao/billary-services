package th.co.readypaper.billary.inventories.inventory.model.dto;

import lombok.Data;
import th.co.readypaper.billary.inventories.common.model.dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InventoryDto {
    private String id;
    private ProductDto product;
    private int locationId;
    private int currentQty;
    private BigDecimal currentPrice;
    private int initialQty;
    private BigDecimal initialPrice;
    private List<InventoryMovementDto> movements;
}
