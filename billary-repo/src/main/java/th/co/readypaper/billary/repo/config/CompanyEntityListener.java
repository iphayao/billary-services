package th.co.readypaper.billary.repo.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.repo.entity.AuditableCompanyEntity;
import th.co.readypaper.billary.repo.repository.CompanyRepository;
import th.co.readypaper.billary.repo.utils.CompanyCode;

import javax.persistence.PrePersist;

@Component
public class CompanyEntityListener {
    @Autowired
    private ObjectFactory<CompanyRepository> holder;

    @PrePersist
    public void touchForCreate(Object target) {
        holder.getObject().findByCode(CompanyCode.get())
                .ifPresent(company -> {
                    AuditableCompanyEntity entity = (AuditableCompanyEntity) target;
                    entity.setCompany(company);
                });
    }

}
