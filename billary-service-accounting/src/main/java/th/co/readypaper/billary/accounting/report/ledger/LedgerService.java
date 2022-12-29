package th.co.readypaper.billary.accounting.report.ledger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import th.co.readypaper.billary.accounting.common.model.AccountingYearlySummary;
import th.co.readypaper.billary.accounting.report.journal.GeneralJournalRepository;
import th.co.readypaper.billary.accounting.report.ledger.model.LedgerDto;
import th.co.readypaper.billary.repo.entity.account.ledger.Ledger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static th.co.readypaper.billary.common.utils.DateUtils.*;

@Slf4j
@Service
public class LedgerService {
    private final LedgerRepository ledgerRepository;
    private final LedgerMapper ledgerMapper;
    private final LedgerBuilder ledgerBuilder;

    private final GeneralJournalRepository generalJournalRepository;

    public LedgerService(LedgerRepository ledgerRepository,
                         LedgerMapper ledgerMapper,
                         LedgerBuilder ledgerBuilder,
                         GeneralJournalRepository generalJournalRepository) {
        this.ledgerRepository = ledgerRepository;
        this.ledgerMapper = ledgerMapper;
        this.ledgerBuilder = ledgerBuilder;
        this.generalJournalRepository = generalJournalRepository;
    }

    public List<LedgerDto> findAllLedgers(Integer year, Integer month) {
        if (year == null || month == null) {
            return ledgerRepository.findAll().stream()
                    .map(ledgerMapper::toDto)
                    .toList();
        } else {
            return ledgerRepository.findByYearAndMonth(year, month, Sort.by("code")).stream()
                    .map(ledgerMapper::toDto)
                    .toList();
        }
    }

    @Transactional
    public List<LedgerDto> createLedger(Integer year, Integer month) {
        if (month == null) {
            List<Ledger> ledgers = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                ledgers.addAll(createLedgerByYearAndMonth(year, i));
            }
            return ledgers.stream()
                    .map(ledgerMapper::toDto)
                    .toList();
        }
        return createLedgerByYearAndMonth(year, month).stream()
                .map(ledgerMapper::toDto)
                .toList();
    }

    public List<AccountingYearlySummary> findLedgerYearlySummary(Integer year) {
        List<AccountingYearlySummary> summaries = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            int month = i;
            var summary = ledgerRepository.countByYearAndMonth(year, month)
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


    private List<Ledger> createLedgerByYearAndMonth(Integer year, Integer month) {
        log.info("Year: {}", year);
        log.info("Month: {}", month);

        LocalDate firstDayOfMonth = firstDayOf(year, month);
        LocalDate lastDayOfMonth = lastDayOf(year, month);

        log.info("First day of month: {}", firstDayOfMonth);
        log.info("Last day of month: {}", lastDayOfMonth);

        LocalDate previousMonth = firstDayOfMonth.minusMonths(1);

        int broughtForwardYear;
        int broughtForwardMonth;

        if (month == 1) {
            broughtForwardYear = year;
            broughtForwardMonth = 0;
        } else {
            broughtForwardYear = previousMonth.getYear();
            broughtForwardMonth = previousMonth.getMonthValue();
        }

        ledgerRepository.deleteByYearAndMonth(year, month)
                .ifPresent(count -> log.info("Count: {}", count));

        Map<String, Ledger> ledgers = new HashMap<>();

        generalJournalRepository.findByDateBetween(firstDayOfMonth, lastDayOfMonth, Sort.by(Sort.Direction.ASC, "date"))
                .forEach(generalJournal -> ledgerBuilder.build(year, month, ledgers, generalJournal));

        return ledgers.values().stream()
                .map(ledgerRepository::save)
                .toList();
    }

}
