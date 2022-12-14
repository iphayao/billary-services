package th.co.readypaper.billary.repo.entity.billing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BillingNoteStatus extends AuditableEntity<Integer> {
    @Id
    private Integer id;
    private String name;
    private String code;
    private String description;

    public BillingNoteStatus(Integer id) {
        this.id = id;
    }

    public static BillingNoteStatus SUBMITTED = new BillingNoteStatus(2);
}
