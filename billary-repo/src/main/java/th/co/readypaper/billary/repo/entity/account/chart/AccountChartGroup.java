package th.co.readypaper.billary.repo.entity.account.chart;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class AccountChartGroup extends AuditableEntity<Integer> {
    @Id
    private Integer id;
    private String name;
    private String code;
    private String description;
}
