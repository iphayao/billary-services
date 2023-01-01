package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.expense.ExpensePaymentType;

public interface ExpensePaymentTypRepository extends JpaRepository<ExpensePaymentType, Integer> {
}
