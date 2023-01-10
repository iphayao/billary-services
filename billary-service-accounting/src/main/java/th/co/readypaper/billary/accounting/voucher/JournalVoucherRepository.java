package th.co.readypaper.billary.accounting.voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import th.co.readypaper.billary.repo.entity.voucher.JournalVoucher;

import java.util.UUID;

public interface JournalVoucherRepository extends JpaRepository<JournalVoucher, UUID>, JpaSpecificationExecutor<JournalVoucher> {
}
