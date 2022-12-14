package th.co.readypaper.billary.repo.entity.account.chart;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableCompanyEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class AccountChart extends AuditableCompanyEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String code;
    private String name;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private AccountChartGroup group;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private AccountChartCategory category;
}
