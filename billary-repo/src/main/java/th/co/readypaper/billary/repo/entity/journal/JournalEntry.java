package th.co.readypaper.billary.repo.entity.journal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableCompanyEntity;
import th.co.readypaper.billary.repo.entity.AuditableEntity;
import th.co.readypaper.billary.repo.entity.contact.Contact;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class JournalEntry extends AuditableCompanyEntity {
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
    private JournalEntryStatus status;
    @OneToMany(mappedBy = "journalEntry",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<JournalEntryLineItem> lineItems;
    private boolean vatInclusive;
    private BigDecimal subTotal;
    private BigDecimal totalDiscount;
    private BigDecimal totalAfterDiscount;
    private BigDecimal exemptVatAmount;
    private BigDecimal vatableAmount;
    private BigDecimal vatAmount;
    private BigDecimal total;
    private String remark;
}
