package th.co.readypaper.billary.repo.entity.voucher;

import lombok.Data;
import lombok.NoArgsConstructor;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
public class JournalVoucherStatus extends AuditableEntity<Integer> {
    @Id
    private Integer id;
    private String name;
    private String code;
    private String description;

    public JournalVoucherStatus(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JournalVoucherStatus that = (JournalVoucherStatus) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    public static final JournalVoucherStatus DRAFT = new JournalVoucherStatus(1);
    public static final JournalVoucherStatus SUBMITTED = new JournalVoucherStatus(2);
    public static final JournalVoucherStatus DELETED = new JournalVoucherStatus(3);

}
