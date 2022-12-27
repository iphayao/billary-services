package th.co.readypaper.billary.accounting.report.journal;

import org.springframework.stereotype.Service;
import th.co.readypaper.billary.accounting.report.journal.model.GeneralJournalDto;

import java.time.LocalDate;
import java.util.List;

import static th.co.readypaper.billary.common.utils.DateUtils.firstDayOf;
import static th.co.readypaper.billary.common.utils.DateUtils.lastDayOf;

@Service
public class GeneralJournalService {
    private final GeneralJournalRepository generalJournalRepository;
    private final GeneralJournalMapper generalJournalMapper;

    public GeneralJournalService(GeneralJournalRepository generalJournalRepository,
                                 GeneralJournalMapper generalJournalMapper) {
        this.generalJournalRepository = generalJournalRepository;
        this.generalJournalMapper = generalJournalMapper;
    }

    public List<GeneralJournalDto> findAllGeneralJournal(Integer year, Integer month) {
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

}
