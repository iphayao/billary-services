package th.co.readypaper.billary.repo.entity.journal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;
import th.co.readypaper.billary.repo.entity.expense.ExpensePaymentType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class JournalEntryPayment extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne
    @JoinColumn(name = "journal_entry_id",
            referencedColumnName = "id")
    private JournalEntry journalEntry;
    private BigDecimal paidAmount;
    private LocalDate paymentDate;
    @ManyToOne
    @JoinColumn(name = "payment_type_id")
    private ExpensePaymentType paymentType;
    private String remark;
}
