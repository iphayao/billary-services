package th.co.readypaper.billary.accounting.report.journal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import th.co.readypaper.billary.accounting.common.model.AccountingYearlySummary;
import th.co.readypaper.billary.accounting.report.journal.model.GeneralJournalDto;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournal;
import th.co.readypaper.billary.repo.repository.InvoiceRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static th.co.readypaper.billary.common.utils.DateUtils.*;

@Slf4j
@Service
public class GeneralJournalService {
    private final GeneralJournalRepository generalJournalRepository;
    private final GeneralJournalMapper generalJournalMapper;
    private final GeneralJournalBuilder generalJournalBuilder;
    private final GeneralJournalExporter generalJournalExporter;

    private final InvoiceRepository invoiceRepository;

    public GeneralJournalService(GeneralJournalRepository generalJournalRepository,
                                 GeneralJournalMapper generalJournalMapper,
                                 GeneralJournalBuilder generalJournalBuilder,
                                 GeneralJournalExporter generalJournalExporter,
                                 InvoiceRepository invoiceRepository) {
        this.generalJournalRepository = generalJournalRepository;
        this.generalJournalMapper = generalJournalMapper;
        this.generalJournalBuilder = generalJournalBuilder;
        this.generalJournalExporter = generalJournalExporter;
        this.invoiceRepository = invoiceRepository;
    }

    public List<GeneralJournalDto> findAllGeneralJournal(Integer year, Integer month) {
        log.info("Find all general journal ...");
        log.info("Year: {}", year);
        log.info("Month: {}", month);
        if (year == null || month == null) {
            return generalJournalRepository.findAll().stream()
                    .map(generalJournalMapper::toDto)
                    .toList();
        } else {
            LocalDate firstDay = firstDayOf(year, month);
            LocalDate lastDay = lastDayOf(year, month);

            return generalJournalRepository.findByDateBetween(firstDay, lastDay).stream()
                    .map(generalJournalMapper::toDto)
                    .toList();
        }
    }

    @Transactional
    public List<GeneralJournalDto> createGeneralJournal(Integer year, Integer month, Integer day) {
        log.info("Create general journal ...");
        log.info("Year: {}", year);
        log.info("Month: {}", month);
        log.info("Day: {}", month);

        LocalDate firstDay;
        LocalDate lastDay;

        if (day != null) {
            firstDay = LocalDate.of(year, month, day);
            lastDay = LocalDate.of(year, month, day);
        } else if (month != null) {
            firstDay = firstDayOf(year, month);
            lastDay = lastDayOf(year, month);
        } else {
            firstDay = firstDayOf(year);
            lastDay = lastDayOf(year);
        }

        log.info("First day : {}", firstDay);
        log.info("Last day  : {}", lastDay);

        generalJournalRepository.deleteByDateBetween(firstDay, lastDay)
                .ifPresent(deleted -> log.info("Deleted: {}", deleted));

        var generalJournals = invoiceRepository.findByIssuedDateBetween(firstDay, lastDay)
                .stream()
                .map(generalJournalBuilder::build)
                .map(generalJournalRepository::save)
                .toList();

        return generalJournals.stream()
                .map(generalJournalMapper::toDto)
                .toList();
    }

    public List<AccountingYearlySummary> findGeneralJournalYearlySummary(Integer year) {
        List<AccountingYearlySummary> summaries = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            int month = i;

            var firstDay = firstDayOf(year, month);
            var lastDay = lastDayOf(year, month);

            var summary = generalJournalRepository.countByDateBetween(firstDay, lastDay)
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

    public Optional<byte[]> exportGeneralJournal(int year, int month) {
        log.info("Export general journal, Year: {}, Month: {}", year, month);

        LocalDate firstDayOfMonth = firstDayOf(year, month);
        LocalDate lastDayOfMonth = lastDayOf(year, month);

        List<GeneralJournal> journals = generalJournalRepository.findByDateBetween(firstDayOfMonth, lastDayOfMonth,
                Sort.by(Sort.Direction.ASC, "date", "documentId"));

        return generalJournalExporter.export(journals);
    }
}
