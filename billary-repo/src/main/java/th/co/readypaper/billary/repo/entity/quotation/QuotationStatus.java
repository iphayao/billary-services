package th.co.readypaper.billary.repo.entity.quotation;

import lombok.Data;
import lombok.NoArgsConstructor;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class QuotationStatus extends AuditableEntity<Integer> {
    @Id
    private Integer id;
    private String name;
    private String code;
    private String description;

    public QuotationStatus(Integer id) {
        this.id = id;
    }

    public static QuotationStatus SUBMITTED = new QuotationStatus(2);

}
