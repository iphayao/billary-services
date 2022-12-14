package th.co.readypaper.billary.repo.entity.expense;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableCompanyEntity;
import th.co.readypaper.billary.repo.entity.contact.Contact;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Expense extends AuditableCompanyEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String documentId;
    private String reference;
    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;
    private LocalDate issuedDate;
    private int lineVatTypeId;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private ExpenseStatus status;
    @OneToMany(mappedBy = "expense",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<ExpenseLineItem> lineItems;
    private boolean vatInclusive;
    private boolean useInputTax;
    private BigDecimal subTotal;
    private BigDecimal totalDiscount;
    private BigDecimal totalAfterDiscount;
    private BigDecimal exemptVatAmount;
    private BigDecimal vatableAmount;
    private BigDecimal vatAmount;
    private BigDecimal total;
    @ManyToOne
    @JoinColumn(name = "withholding_tax_percent_id")
    private WithholdingTaxPercent withholdingTaxPercent;
    private BigDecimal withholdingTaxAmount;
    private BigDecimal paymentAmount;
    private String remark;
    @OneToOne(mappedBy = "expense",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private ExpensePayment payment;
}
