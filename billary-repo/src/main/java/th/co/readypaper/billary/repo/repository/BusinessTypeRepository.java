package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.contact.BusinessType;

public interface BusinessTypeRepository extends JpaRepository<BusinessType, Integer> {
}
