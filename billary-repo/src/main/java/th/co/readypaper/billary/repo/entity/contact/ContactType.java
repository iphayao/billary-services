package th.co.readypaper.billary.repo.entity.contact;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class ContactType extends AuditableEntity<Integer> {
    @Id
    private Integer id;
    private String name;
    private String description;
}
