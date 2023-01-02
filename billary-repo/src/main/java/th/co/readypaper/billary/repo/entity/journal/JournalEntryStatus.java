package th.co.readypaper.billary.repo.entity.journal;

import lombok.Data;
import lombok.NoArgsConstructor;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
public class JournalEntryStatus extends AuditableEntity<Integer> {
    @Id
    private Integer id;
    private String name;
    private String code;
    private String description;

    public JournalEntryStatus(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JournalEntryStatus that = (JournalEntryStatus) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    public static final JournalEntryStatus DRAFT = new JournalEntryStatus(1);
    public static final JournalEntryStatus SUBMITTED = new JournalEntryStatus(2);
    public static final JournalEntryStatus PAID = new JournalEntryStatus(3);
    public static final JournalEntryStatus DELETED = new JournalEntryStatus(4);
    public static final JournalEntryStatus VOIDED = new JournalEntryStatus(5);
}
