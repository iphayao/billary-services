package th.co.readypaper.billary.repo.entity.account.journal;

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
public class GeneralJournalCredit extends AuditableEntity<UUID>  {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "general_journal_id")
    private GeneralJournal generalJournal;
    private String code;
    @Column(name = "description")
    private String desc;
    private BigDecimal amount;
}
