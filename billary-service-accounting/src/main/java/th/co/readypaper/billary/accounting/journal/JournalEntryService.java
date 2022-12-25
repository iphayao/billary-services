package th.co.readypaper.billary.accounting.journal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.accounting.journal.model.dto.JournalEntryDto;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.repo.entity.journal.JournalEntry;
import th.co.readypaper.billary.common.service.DocumentIdBaseService;
import th.co.readypaper.billary.repo.repository.CompanyRepository;
import th.co.readypaper.billary.repo.repository.DocumentRepository;
import th.co.readypaper.billary.repo.repository.DocumentSerialRepository;

import java.util.Map;

@Slf4j
@Service
public class JournalEntryService extends DocumentIdBaseService<JournalEntry> {
    private final JournalEntryRepository journalEntryRepository;
    private final JournalEntryMapper journalEntryMapper;

    public JournalEntryService(JournalEntryRepository journalEntryRepository,
                               CompanyRepository companyRepository,
                               DocumentRepository documentRepository,
                               DocumentSerialRepository documentSerialRepository, JournalEntryMapper journalEntryMapper) {
        super(companyRepository, documentRepository, documentSerialRepository, 8);
        this.journalEntryRepository = journalEntryRepository;
        this.journalEntryMapper = journalEntryMapper;
    }

    public ResultPage<JournalEntryDto> findJournals(Integer page, Integer limit, Map<String, Object> params) {
        log.info("Find all journal entries, page: {}, limit: {}, params: {}", page, limit, params);
        var pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "documentId"));

        var journalEntryPage = journalEntryRepository.findAll(filterByParams(params), pageable);
        var journalEntries = journalEntryPage.map(journalEntryMapper::toDto).toList();

        return ResultPage.of(journalEntries, page, limit, journalEntryPage.getTotalElements());
    }

}
