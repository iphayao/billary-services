package th.co.readypaper.billary.repo.entity.journal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;
import th.co.readypaper.billary.repo.entity.account.chart.AccountChart;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class JournalEntryLineItem extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "journal_entry_id",
            referencedColumnName = "id")
    private JournalEntry journalEntry;
    @ManyToOne
    @JoinColumn(name = "debit_account_chart_id",
            referencedColumnName = "id")
    private AccountChart debitAccountChart;
    @ManyToOne
    @JoinColumn(name = "credit_account_chart_id",
            referencedColumnName = "id")
    private AccountChart creditAccountChart;
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
