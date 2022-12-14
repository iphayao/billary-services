package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.account.chart.AccountChart;

import java.util.Optional;
import java.util.UUID;

public interface AccountChartRepository extends JpaRepository<AccountChart, UUID> {
    Optional<AccountChart> findByCode(String code);
}
