package th.co.readypaper.billary.repo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import th.co.readypaper.billary.repo.config.CompanyEntityListener;
import th.co.readypaper.billary.repo.entity.company.Company;

import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Data
@Slf4j
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@EntityListeners(CompanyEntityListener.class)
public abstract class AuditableCompanyEntity extends AuditableEntity<UUID> {

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
