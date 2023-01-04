package th.co.readypaper.billary.accounting.report.ledger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import th.co.readypaper.billary.accounting.common.model.AccountingYearlySummary;
import th.co.readypaper.billary.accounting.report.ledger.model.LedgerDto;
import th.co.readypaper.billary.repo.entity.account.ledger.Ledger;
import th.co.readypaper.billary.repo.repository.AccountChartRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static th.co.readypaper.billary.common.utils.DateUtils.*;

@Slf4j
@Service
public class LedgerService {
    private final AccountChartRepository accountChartRepository;
    private final LedgerRepository ledgerRepository;
    private final LedgerMapper ledgerMapper;
    private final LedgerBuilder ledgerBuilder;
    private final LedgerExporter ledgerExporter;

    public LedgerService(AccountChartRepository accountChartRepository,
                         LedgerRepository ledgerRepository,
                         LedgerMapper ledgerMapper,
                         LedgerBuilder ledgerBuilder,
                         LedgerExporter ledgerExporter) {
        this.accountChartRepository = accountChartRepository;
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

    public Optional<LedgerDto> findLedgerById(UUID id) {
        log.info("Find ledger by ID: {}", id);
        return ledgerRepository.findById(id)
                .map(ledgerMapper::toDto);
    }

    public Optional<LedgerDto> createLedger(LedgerDto ledgerDto) {
        log.info("Create ledger by request");
        Ledger ledgerEntity = ledgerMapper.toEntity(ledgerDto);

        accountChartRepository.findByCode(ledgerEntity.getCode())
                .ifPresent(accountChart -> ledgerEntity.setDesc(accountChart.getName()));
        log.info("Account Code: {}", ledgerEntity.getCode());
        log.info("Account Desc: {}", ledgerEntity.getDesc());

        Ledger savedLedger = ledgerRepository.save(ledgerEntity);
        return Optional.of(savedLedger)
                .map(ledgerMapper::toDto);
    }

    @Transactional
    public List<LedgerDto> createLedger(Integer year, Integer month) {
        log.info("Create ledger, year: {}, month: {}", year, month);
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
        log.info("Find ledger yearly summary, year: {}", year);
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
        var ledgers = ledgerRepository.findByYearAndMonth(year, month,
                Sort.by(Sort.Direction.ASC, "code"));

        return ledgerExporter.export(ledgers);
    }

    public Optional<LedgerDto> updateLedgerById(UUID id, LedgerDto updateLedger) {
        log.info("Update ledger by ID: {}", id);
        return ledgerRepository.findById(id)
                .map(ledger -> ledgerMapper.update(updateLedger, ledger))
                .map(ledgerRepository::save)
                .map(ledgerMapper::toDto);
    }

    public void deleteLedgerById(UUID id) {
        log.info("Delete ledger ID: {}", id);
        ledgerRepository.deleteById(id);
    }
}
