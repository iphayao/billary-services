package th.co.readypaper.billary.repo.entity.account.ledger;

import lombok.*;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LedgerCredit extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "ledger_id")
    private Ledger ledger;
    private LocalDate date;
    @Column(name = "description")
    private String desc;
    private BigDecimal amount;
    private UUID reference;
}
