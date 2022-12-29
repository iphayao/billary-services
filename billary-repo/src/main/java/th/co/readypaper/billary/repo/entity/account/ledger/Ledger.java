package th.co.readypaper.billary.repo.entity.account.ledger;

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
public class Ledger extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;
    private String code;
    @Column(name = "description")
    private String desc;
    private int year;
    private int month;
    @OneToMany(mappedBy = "ledger",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<LedgerDebit> debits;
    @OneToMany(mappedBy = "ledger",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<LedgerCredit> credits;
    private BigDecimal sumDebit;
    private BigDecimal sumCredit;
    private BigDecimal diffSumDebitCredit;
}
