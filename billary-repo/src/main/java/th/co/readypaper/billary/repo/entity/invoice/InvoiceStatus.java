package th.co.readypaper.billary.repo.entity.invoice;

import lombok.Data;
import lombok.NoArgsConstructor;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
public class InvoiceStatus extends AuditableEntity<Integer> {
    @Id
    private Integer id;
    private String name;
    private String code;
    private String description;
    public InvoiceStatus(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InvoiceStatus that = (InvoiceStatus) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    public static InvoiceStatus DRAFT = new InvoiceStatus(1);
    public static InvoiceStatus SUBMITTED = new InvoiceStatus(2);
    public static InvoiceStatus AUTHORIZED = new InvoiceStatus(3);
    public static InvoiceStatus DELETED = new InvoiceStatus(4);
    public static InvoiceStatus VOIDED = new InvoiceStatus(5);

}
