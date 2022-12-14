package th.co.readypaper.billary.repo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.company.Company;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByCode(String companyCode);
}
