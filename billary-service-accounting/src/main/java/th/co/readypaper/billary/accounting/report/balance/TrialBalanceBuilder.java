package th.co.readypaper.billary.accounting.report.balance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.accounting.report.ledger.LedgerRepository;
import th.co.readypaper.billary.repo.entity.account.balance.TrialBalance;
import th.co.readypaper.billary.repo.entity.account.balance.TrialBalanceEntry;
import th.co.readypaper.billary.repo.entity.account.ledger.Ledger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TrialBalanceBuilder {
    private final TrialBalanceRepository trialBalanceRepository;
    private final LedgerRepository ledgerRepository;

    public TrialBalanceBuilder(TrialBalanceRepository trialBalanceRepository,
                               LedgerRepository ledgerRepository) {
        this.trialBalanceRepository = trialBalanceRepository;
        this.ledgerRepository = ledgerRepository;
    }

    public TrialBalance buildByYearAndMonth(Integer year, Integer month) {
        log.info("Build trial balance by year: {}, and month: {}", year, month);
        var trialBalance = new TrialBalance();
        trialBalance.setYear(year);
        trialBalance.setMonth(month);
        trialBalance.setEntries(new ArrayList<>());
        trialBalance.setDebitAmount(BigDecimal.ZERO);
        trialBalance.setCreditAmount(BigDecimal.ZERO);

        trialBalanceRepository.deleteByYearAndMonth(year, month)
                .ifPresent(count -> log.info("Trial balance deleted: {} rows", count));

        ledgerRepository.findByYearAndMonth(year, month, Sort.by("code"))
                .forEach(ledger -> {
                    List<TrialBalanceEntry> entries = trialBalance.getEntries();
                    entries.add(TrialBalanceEntry.builder()
                            .trialBalance(trialBalance)
                            .accountName(ledger.getDesc())
                            .accountCode(ledger.getCode())
                            .debit(setDebitAmount(ledger))
                            .credit(setCreditAmount(ledger))
                            .build());
                    trialBalance.setEntries(entries);
                    trialBalance.setDebitAmount(sumOfDebit(entries));
                    trialBalance.setCreditAmount(sumOfCredit(entries));
                });

        return trialBalanceRepository.save(trialBalance);
    }

    private BigDecimal setDebitAmount(Ledger ledger) {
        if (isGreaterThan(ledger.getSumDebit(), ledger.getSumCredit())) {
            return ledger.getDiffSumDebitCredit();
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal setCreditAmount(Ledger ledger) {
        if (isGreaterThan(ledger.getSumCredit(), ledger.getSumDebit())) {
            return ledger.getDiffSumDebitCredit();
        }
        return BigDecimal.ZERO;
    }

    private static boolean isGreaterThan(BigDecimal ledger, BigDecimal ledger1) {
        return ledger.abs().compareTo(ledger1.abs()) > 0;
    }

    private BigDecimal sumOfDebit(List<TrialBalanceEntry> entries) {
        return entries.stream().map(TrialBalanceEntry::getDebit)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_DOWN);
    }

    private BigDecimal sumOfCredit(List<TrialBalanceEntry> entries) {
        return entries.stream().map(TrialBalanceEntry::getCredit)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_DOWN);
    }

}
