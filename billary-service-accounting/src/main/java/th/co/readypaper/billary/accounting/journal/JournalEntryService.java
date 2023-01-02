package th.co.readypaper.billary.accounting.journal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.accounting.journal.model.dto.JournalEntryDto;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.repo.entity.journal.JournalEntry;
import th.co.readypaper.billary.common.service.DocumentIdBaseService;
import th.co.readypaper.billary.repo.entity.journal.JournalEntryStatus;
import th.co.readypaper.billary.repo.repository.CompanyRepository;
import th.co.readypaper.billary.repo.repository.DocumentRepository;
import th.co.readypaper.billary.repo.repository.DocumentSerialRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public Optional<JournalEntryDto> findJournalEntryById(UUID id) {
        log.info("Find journal entry by ID: {}", id);
        return journalEntryRepository.findById(id)
                .map(journalEntryMapper::toDto);
    }

    public Optional<JournalEntryDto> createJournalEntry(JournalEntryDto newJournalEntryDto) {
        log.info("Create new journal entry, document ID: {}", newJournalEntryDto.getDocumentId());
        JournalEntry newJournalEntryEntity = journalEntryMapper.toEntity(newJournalEntryDto);
        if (newJournalEntryEntity.getStatus() == null || newJournalEntryEntity.getStatus().equals(JournalEntryStatus.DRAFT)) {
            newJournalEntryEntity.setStatus(JournalEntryStatus.SUBMITTED);
        }

        newJournalEntryEntity.setLineItems(newJournalEntryEntity.getLineItems().stream()
                .peek(journalEntryLineItem -> journalEntryLineItem.setJournalEntry(newJournalEntryEntity))
                .collect(Collectors.toList()));

        JournalEntry savedJournalEntry = journalEntryRepository.save(newJournalEntryEntity);
        updateDocumentId(savedJournalEntry.getDocumentId());

        assert savedJournalEntry.getId() != null;
        return journalEntryRepository.findById(savedJournalEntry.getId())
                .map(journalEntryMapper::toDto);
    }
}
