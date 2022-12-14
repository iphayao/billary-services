package th.co.readypaper.billary.repo.entity.expense;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class ExpensePayment extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne
    @JoinColumn(name = "expense_id",
            referencedColumnName = "id")
    private Expense expense;
    private BigDecimal paidAmount;
    private LocalDate paymentDate;
    @ManyToOne
    @JoinColumn(name = "payment_type_id")
    private ExpensePaymentType paymentType;
    private String remark;
}
