package th.co.readypaper.billary.repo.entity.expense;

import lombok.Data;
import lombok.NoArgsConstructor;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class ExpenseStatus extends AuditableEntity<Integer> {
    @Id
    private Integer id;
    private String name;
    private String code;
    private String description;

    public ExpenseStatus(Integer id) {
        this.id = id;
    }

    public static final ExpenseStatus DRAFT = new ExpenseStatus(1);
    public static final ExpenseStatus SUBMITTED = new ExpenseStatus(2);
    public static final ExpenseStatus PAID = new ExpenseStatus(3);
    public static final ExpenseStatus DELETED = new ExpenseStatus(4);
    public static final ExpenseStatus VOIDED = new ExpenseStatus(5);
}
