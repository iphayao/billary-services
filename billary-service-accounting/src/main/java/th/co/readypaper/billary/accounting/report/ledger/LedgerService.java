package th.co.readypaper.billary.accounting.report.ledger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import th.co.readypaper.billary.accounting.common.model.AccountingYearlySummary;
import th.co.readypaper.billary.accounting.report.ledger.model.LedgerDto;
import th.co.readypaper.billary.repo.entity.account.ledger.Ledger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static th.co.readypaper.billary.common.utils.DateUtils.*;

@Slf4j
@Service
public class LedgerService {
    private final LedgerRepository ledgerRepository;
    private final LedgerMapper ledgerMapper;
    private final LedgerBuilder ledgerBuilder;
    private final LedgerExporter ledgerExporter;

    public LedgerService(LedgerRepository ledgerRepository,
                         LedgerMapper ledgerMapper,
                         LedgerBuilder ledgerBuilder,
                         LedgerExporter ledgerExporter) {
        this.ledgerRepository = ledgerRepository;
        this.ledgerMapper = ledgerMapper;
        this.ledgerBuilder = ledgerBuilder;
        this.ledgerExporter = ledgerExporter;
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
        var ledgers = new ArrayList<Ledger>();

        if (month == null) {
            for (int i = 1; i <= 12; i++) {
                ledgers.addAll(ledgerBuilder.buildByYearAndMonth(year, i));
            }
        } else {
            ledgers.addAll(ledgerBuilder.buildByYearAndMonth(year, month));
        }

        return ledgers.stream()
                .map(ledgerMapper::toDto)
                .toList();
    }

    public List<AccountingYearlySummary> findLedgerYearlySummary(Integer year) {
        var summaries = new ArrayList<AccountingYearlySummary>();

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

    public Optional<byte[]> exportLedger(Integer year, Integer month) {
        log.info("Export ledger, Year: {}, Month: {}", year, month);

        LocalDate firstDayOfMonth = firstDayOf(year, month);
        LocalDate lastDayOfMonth = lastDayOf(year, month);

        var ledgers = ledgerRepository.findByYearAndMonth(year, month,
                Sort.by(Sort.Direction.ASC, "code"));

        return ledgerExporter.export(ledgers);
    }
}
