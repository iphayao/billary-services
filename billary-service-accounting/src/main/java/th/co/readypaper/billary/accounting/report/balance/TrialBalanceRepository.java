package th.co.readypaper.billary.accounting.report.balance;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.account.balance.TrialBalance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrialBalanceRepository extends JpaRepository<TrialBalance, UUID> {
    TrialBalance findByYearAndMonth(Integer year, Integer month);

    List<TrialBalance> findByYear(Integer year);

    Optional<Long> countByYearAndMonth(Integer year, int month);

    Optional<Long> deleteByYearAndMonth(Integer year, Integer month);
}
