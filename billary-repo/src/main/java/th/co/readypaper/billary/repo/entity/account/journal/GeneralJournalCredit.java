package th.co.readypaper.billary.repo.entity.account.journal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class GeneralJournalCredit extends AuditableEntity<UUID>  {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "general_journal_id")
    private GeneralJournal generalJournal;
    private String code;
    private String description;
    private BigDecimal amount;
}
