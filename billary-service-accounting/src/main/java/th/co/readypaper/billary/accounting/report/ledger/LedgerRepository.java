package th.co.readypaper.billary.accounting.report.ledger;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.account.ledger.Ledger;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LedgerRepository extends JpaRepository<Ledger, UUID> {
    List<Ledger> findByYearAndMonth(Integer year, Integer month, Sort code);
    List<Ledger> findByYearAndMonth(Integer year, Integer month);

    Optional<Long> deleteByYearAndMonth(Integer year, Integer month);

    Optional<Long> countByYearAndMonth(Integer year, int month);
}
