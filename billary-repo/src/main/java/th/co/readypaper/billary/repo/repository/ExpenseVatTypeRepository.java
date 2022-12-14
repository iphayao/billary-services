package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.expense.ExpenseVatType;

public interface ExpenseVatTypeRepository extends JpaRepository<ExpenseVatType, Integer> {
}
