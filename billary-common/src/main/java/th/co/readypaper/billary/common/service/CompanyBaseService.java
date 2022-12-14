package th.co.readypaper.billary.common.service;

import th.co.readypaper.billary.repo.utils.CompanyCode;
import th.co.readypaper.billary.repo.entity.company.Company;
import th.co.readypaper.billary.repo.repository.CompanyRepository;

import java.util.UUID;

public class CompanyBaseService {
    private final CompanyRepository companyRepository;

    public CompanyBaseService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    protected UUID getCompanyId() {
        return companyRepository.findByCode(CompanyCode.get())
                .map(Company::getId)
                .orElseThrow();
    }

}
