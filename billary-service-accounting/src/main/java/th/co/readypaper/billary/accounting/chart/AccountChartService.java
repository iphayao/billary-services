package th.co.readypaper.billary.accounting.chart;

import org.springframework.stereotype.Service;
import th.co.readypaper.billary.common.model.ApiResponse;
import th.co.readypaper.billary.common.model.dto.accounting.chart.AccountChartDto;
import th.co.readypaper.billary.repo.repository.AccountChartRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountChartService {
    private final AccountChartRepository accountChartRepository;
    private final AccountChartMapper accountChartMapper;

    public AccountChartService(AccountChartRepository accountChartRepository,
                               AccountChartMapper accountChartMapper) {
        this.accountChartRepository = accountChartRepository;
        this.accountChartMapper = accountChartMapper;
    }

    public List<AccountChartDto> findAccountCharts() {
        return accountChartRepository.findAll().stream()
                .map(accountChartMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<AccountChartDto> findAccountChartByCode(String code) {
        return accountChartRepository.findByCode(code)
                .map(accountChartMapper::toDto);
    }
}
