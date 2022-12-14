package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.expense.WithholdingTaxPercent;

public interface WithholdingTaxPercentRepository extends JpaRepository<WithholdingTaxPercent, Integer> {
}
