package th.co.readypaper.billary.repo.entity.receipt;

import lombok.Data;
import lombok.NoArgsConstructor;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class ReceiptStatus extends AuditableEntity<Integer> {
    @Id
    private Integer id;
    private String name;
    private String code;
    private String description;

    public ReceiptStatus(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        //if (!super.equals(o)) return false;

        ReceiptStatus that = (ReceiptStatus) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    public static ReceiptStatus DRAFT = new ReceiptStatus(1);
    public static ReceiptStatus SUBMITTED = new ReceiptStatus(2);
    public static ReceiptStatus AUTHORIZED = new ReceiptStatus(3);
    public static ReceiptStatus PAID = new ReceiptStatus(4);
    public static ReceiptStatus DELETED = new ReceiptStatus(5);
    public static ReceiptStatus VOIDED = new ReceiptStatus(6);

}
