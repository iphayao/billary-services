package th.co.readypaper.billary.repo.entity.account.journal;

import lombok.Data;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class GeneralJournal extends AuditableEntity<UUID> {
    @Id
    private UUID id;
    private LocalDate date;
    private String description;
    private String type;
    private UUID reference;
    private String documentId;
    @OneToMany(mappedBy = "generalJournal",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<GeneralJournalDebit> debit;
    @OneToMany(mappedBy = "generalJournal",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<GeneralJournalCredit> credit;

}
