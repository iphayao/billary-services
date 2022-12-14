package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.receipt.ReceiptPaymentType;

public interface PaymentTypeRepository extends JpaRepository<ReceiptPaymentType, Integer> {
}
