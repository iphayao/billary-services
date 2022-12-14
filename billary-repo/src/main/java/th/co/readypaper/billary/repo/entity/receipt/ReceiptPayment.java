package th.co.readypaper.billary.repo.entity.receipt;

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
public class ReceiptPayment extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne
    @JoinColumn(name = "receipt_id",
            referencedColumnName = "id")
    private Receipt receipt;
    private BigDecimal paidAmount;
    private LocalDate paymentDate;
    @ManyToOne
    @JoinColumn(name = "payment_type_id")
    private ReceiptPaymentType paymentType;
    private String remark;
}
