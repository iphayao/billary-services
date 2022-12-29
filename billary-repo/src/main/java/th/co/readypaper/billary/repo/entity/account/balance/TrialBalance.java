package th.co.readypaper.billary.repo.entity.account.balance;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class TrialBalance extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;
    private Integer year;
    private Integer month;
    @OneToMany(mappedBy = "trialBalance",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<TrialBalanceEntry> entries;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
}
