package th.co.readypaper.billary.repo.entity.journal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import th.co.readypaper.billary.repo.entity.AuditableEntity;
import th.co.readypaper.billary.repo.entity.expense.ExpenseStatus;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JournalEntryStatus extends AuditableEntity<Integer> {
    @Id
    private Integer id;
    private String name;
    private String code;
    private String description;

    public JournalEntryStatus(Integer id) {
        this.id = id;
    }

    public static final ExpenseStatus DRAFT = new ExpenseStatus(1);
    public static final ExpenseStatus SUBMITTED = new ExpenseStatus(2);
    public static final ExpenseStatus PAID = new ExpenseStatus(3);
    public static final ExpenseStatus DELETED = new ExpenseStatus(4);
    public static final ExpenseStatus VOIDED = new ExpenseStatus(5);
}
