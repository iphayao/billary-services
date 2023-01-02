package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournal;
import th.co.readypaper.billary.repo.entity.receipt.Receipt;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReceiptRepository extends JpaRepository<Receipt, UUID>, JpaSpecificationExecutor<Receipt> {
    List<Receipt> findByIssuedDateBetween(LocalDate startDate, LocalDate endDate);
}
