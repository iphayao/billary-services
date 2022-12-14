package th.co.readypaper.billary.repo.entity.invoice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;
import th.co.readypaper.billary.repo.entity.product.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class InvoiceLineItem extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "invoice_id",
            referencedColumnName = "id")
    private Invoice invoice;
    private int itemOrder;
    @ManyToOne
    @JoinColumn(name = "item_product_id")
    private Product product;
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
