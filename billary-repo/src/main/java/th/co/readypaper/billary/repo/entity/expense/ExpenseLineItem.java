package th.co.readypaper.billary.repo.entity.expense;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;
import th.co.readypaper.billary.repo.entity.account.chart.AccountChart;
import th.co.readypaper.billary.repo.entity.product.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class ExpenseLineItem extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "expense_id",
            referencedColumnName = "id")
    private Expense expense;
    @ManyToOne
    @JoinColumn(name = "account_chart_id",
            referencedColumnName = "id")
    private AccountChart accountChart;
    private int itemOrder;
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
