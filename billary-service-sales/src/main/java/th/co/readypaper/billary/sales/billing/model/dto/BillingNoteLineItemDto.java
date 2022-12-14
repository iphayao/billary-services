package th.co.readypaper.billary.sales.billing.model.dto;

import lombok.Data;
import th.co.readypaper.billary.sales.common.model.dto.ProductDto;

import java.math.BigDecimal;

@Data
public class BillingNoteLineItemDto {
    private String id;
    private int itemOrder;
    private ProductDto product;
    private String description;
    private int quantity;
    private String unitName;
    private int vatTypeId;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal exemptVatAmount;
    private BigDecimal vatableAmount;
    private BigDecimal vatAmount;
    private BigDecimal lineAmount;
}
