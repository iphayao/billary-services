package th.co.readypaper.billary.sales.billing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.repo.entity.billing.BillingNote;
import th.co.readypaper.billary.repo.entity.billing.BillingNoteStatus;
import th.co.readypaper.billary.repo.repository.BillingNoteRepository;
import th.co.readypaper.billary.repo.repository.CompanyRepository;
import th.co.readypaper.billary.sales.billing.model.dto.BillingNoteDto;
import th.co.readypaper.billary.common.service.DocumentIdBaseService;
import th.co.readypaper.billary.sales.common.model.document.DocumentType;
import th.co.readypaper.billary.repo.repository.DocumentRepository;
import th.co.readypaper.billary.repo.repository.DocumentSerialRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BillingNoteService extends DocumentIdBaseService {
    private final BillingNoteRepository billingNoteRepository;
    private final BillingNoteMapper billingNoteMapper;
    private final BillingNoteDocument billingNoteDocument;

    public BillingNoteService(BillingNoteRepository billingNoteRepository,
                              CompanyRepository companyRepository,
                              DocumentRepository documentRepository,
                              DocumentSerialRepository documentSerialRepository,
                              BillingNoteMapper billingNoteMapper,
                              BillingNoteDocument billingNoteDocument) {
        super(companyRepository, documentRepository, documentSerialRepository, 4);
        this.billingNoteRepository = billingNoteRepository;
        this.billingNoteMapper = billingNoteMapper;
        this.billingNoteDocument = billingNoteDocument;
    }

    public ResultPage<BillingNoteDto> findAllBillingNotes(Integer page, Integer limit, Map<String, Object> params) {
        log.info("Find all billing note, page: {}, limit: {}, params: {}", page, limit, params);
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "documentId"));

        Page<BillingNote> billingNotePage = billingNoteRepository.findAll(pageable);
        List<BillingNoteDto> billingNotes = billingNotePage.map(billingNoteMapper::toDto).toList();

        return ResultPage.of(billingNotes, page, limit, (int) billingNotePage.getTotalElements());
    }

    public Optional<BillingNoteDto> findBillingNoteById(UUID id) {
        log.info("Find billing note by ID: {}", id);
        return billingNoteRepository.findById(id)
                .map(billingNoteMapper::toDto);
    }

    public Optional<BillingNoteDto> createNewBillingNote(BillingNoteDto newBillingNote) {
        log.info("Create new billing note document ID: {}", newBillingNote.getDocumentId());
        BillingNote newBillingNoteEntity = billingNoteMapper.toEntity(newBillingNote);
        newBillingNoteEntity.setStatus(BillingNoteStatus.SUBMITTED);

        newBillingNoteEntity.setLineItems(newBillingNoteEntity.getLineItems().stream()
                .map(billingNoteLineItem -> {
                    billingNoteLineItem.setBillingNote(newBillingNoteEntity);
                    return billingNoteLineItem;
                }).collect(Collectors.toList()));

        BillingNote savedBillingNoteEntity = billingNoteRepository.save(newBillingNoteEntity);
        updateDocumentId(savedBillingNoteEntity.getDocumentId());

        return Optional.of(savedBillingNoteEntity).map(billingNoteMapper::toDto);
    }

    public Optional<BillingNoteDto> updateBillingNoteById(UUID id, BillingNoteDto updateBillingNote) {
        log.info("Update billing note by ID: {}", id);
        BillingNote updateBillingNoteEntity = billingNoteMapper.toEntity(updateBillingNote);
        return billingNoteRepository.findById(id)
                .map(billingNote -> {
                    BillingNote mappedBillingNote = billingNoteMapper.update(billingNote, updateBillingNoteEntity);
                    mappedBillingNote.setLineItems(mappedBillingNote.getLineItems().stream()
                            .map(billingNoteLineItem -> {
                                billingNoteLineItem.setBillingNote(mappedBillingNote);
                                return billingNoteLineItem;
                            }).collect(Collectors.toList()));
                    return billingNoteRepository.save(mappedBillingNote);
                })
                .map(billingNoteMapper::toDto);
    }

    public void deleteBillingNoteById(UUID id) {
        log.info("Delete billing note by ID: {}", id);
        billingNoteRepository.findById(id)
                .ifPresent(billingNoteRepository::delete);
    }

    public Optional<byte[]> generateDocumentById(UUID id, DocumentType documentType, boolean isSignAndStamp) {
        return billingNoteRepository.findById(id)
                .flatMap(billingNote -> {
                            try {
                                return billingNoteDocument.generate(
                                        billingNote.getCompany(),
                                        documentType,
                                        billingNoteMapper.toDocument(billingNote),
                                        billingNote.getReference(),
                                        isSignAndStamp
                                );
                            } catch (Exception e) {
                                log.error("Error: {}", e.getMessage(), e);
                            }
                            return Optional.empty();
                        }
                );
    }

}
