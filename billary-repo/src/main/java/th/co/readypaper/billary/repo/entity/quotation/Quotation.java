package th.co.readypaper.billary.repo.entity.quotation;

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
public class Quotation extends AuditableCompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String documentId;
    private String reference;
    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;
    private LocalDate issuedDate;
    private LocalDate dueDate;
    private int creditDay;
    private int lineVatTypeId;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private QuotationStatus status;
    private String saleName;
    private String saleChannel;
    private boolean sentToContact;
    private LocalDate expectPaymentDate;
    private LocalDate plannedPaymentDate;
    @OneToMany(mappedBy = "quotation",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<QuotationLineItem> lineItems;
    private BigDecimal subTotal;
    private BigDecimal totalDiscount;
    private BigDecimal totalAfterDiscount;
    private BigDecimal exemptVatAmount;
    private BigDecimal vatableAmount;
    private BigDecimal vatAmount;
    private BigDecimal total;
    private String remark;
}
