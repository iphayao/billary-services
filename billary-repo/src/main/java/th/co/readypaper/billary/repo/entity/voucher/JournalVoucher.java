package th.co.readypaper.billary.repo.entity.voucher;

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
public class JournalVoucher extends AuditableCompanyEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String documentId;
    private String reference;
    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;
    private LocalDate issuedDate;
    private String description;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private JournalVoucherStatus status;
    @OneToMany(mappedBy = "journalVoucher",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<JournalVoucherLineItem> lineItems;
    private BigDecimal totalDebitAmount;
    private BigDecimal totalCreditAmount;
    private String remark;
}
