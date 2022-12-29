package th.co.readypaper.billary.repo.entity.account.balance;

import lombok.*;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TrialBalanceEntry extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "trial_balance_id")
    private TrialBalance trialBalance;
    private String accountName;
    private String accountCode;
    private BigDecimal debit;
    private BigDecimal credit;
}
