package th.co.readypaper.billary.accounting.report.balance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import th.co.readypaper.billary.accounting.common.model.AccountingYearlySummary;
import th.co.readypaper.billary.accounting.report.balance.model.TrialBalanceDto;
import th.co.readypaper.billary.repo.entity.account.balance.TrialBalance;

import java.util.ArrayList;
import java.util.List;

import static th.co.readypaper.billary.common.utils.DateUtils.getFullMonthOf;

@Slf4j
@Service
public class TrialBalanceService {
    private final TrialBalanceRepository trialBalanceRepository;
    private final TrialBalanceMapper trialBalanceMapper;
    private final TrialBalanceBuilder trialBalanceBuilder;

    public TrialBalanceService(TrialBalanceRepository trialBalanceRepository,
                               TrialBalanceMapper trialBalanceMapper,
                               TrialBalanceBuilder trialBalanceBuilder) {
        this.trialBalanceRepository = trialBalanceRepository;
        this.trialBalanceMapper = trialBalanceMapper;
        this.trialBalanceBuilder = trialBalanceBuilder;
    }

    public List<TrialBalanceDto> findAllTrialBalances(Integer year, Integer month) {
        List<TrialBalance> trialBalances;
        if (year == null && month == null) {
            trialBalances = trialBalanceRepository.findAll();
        } else if (year != null && month == null) {
            trialBalances = trialBalanceRepository.findByYear(year);
        } else {
            trialBalances = trialBalanceRepository.findByYearAndMonth(year, month);
        }
        return trialBalances.stream()
                .map(trialBalanceMapper::toDto)
                .toList();
    }

    public List<AccountingYearlySummary> findTrialBalanceYearlySummary(Integer year) {
        log.info("Find trial balance yearly summary, year: {}", year);
        var summaries = new ArrayList<AccountingYearlySummary>();

        for (int i = 1; i <= 12; i++) {
            int month = i;
            var summary = trialBalanceRepository.countByYearAndMonth(year, month)
                    .map(count -> AccountingYearlySummary.builder()
                            .index(month)
                            .year(year)
                            .month(getFullMonthOf(month))
                            .entryCount(count.intValue())
                            .build());
            summary.ifPresent(summaries::add);
        }

        return summaries;
    }

    @Transactional
    public List<TrialBalanceDto> createTrialBalance(Integer year, Integer month) {
        log.info("Create trial balance, year: {}, month: {}", year, month);
        var trialBalances = new ArrayList<TrialBalance>();

        if (month == null) {
            for (int i = 1; i <= 12; i++) {
                trialBalances.add(trialBalanceBuilder.buildByYearAndMonth(year, i));
            }
        } else {
            trialBalances.add(trialBalanceBuilder.buildByYearAndMonth(year, month));
        }

        return trialBalances.stream()
                .map(trialBalanceMapper::toDto)
                .toList();
    }

}
