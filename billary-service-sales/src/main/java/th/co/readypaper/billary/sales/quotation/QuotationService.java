package th.co.readypaper.billary.sales.quotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.repo.entity.quotation.Quotation;
import th.co.readypaper.billary.repo.entity.quotation.QuotationStatus;
import th.co.readypaper.billary.repo.repository.CompanyRepository;
import th.co.readypaper.billary.repo.repository.QuotationRepository;
import th.co.readypaper.billary.common.service.DocumentIdBaseService;
import th.co.readypaper.billary.sales.common.model.document.DocumentType;
import th.co.readypaper.billary.repo.repository.DocumentRepository;
import th.co.readypaper.billary.repo.repository.DocumentSerialRepository;
import th.co.readypaper.billary.sales.quotation.model.dto.QuotationDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

@Slf4j
@Service
public class QuotationService extends DocumentIdBaseService<Quotation> {
    private final QuotationRepository quotationRepository;
    private final QuotationMapper quotationMapper;
    private final QuotationDocument quotationDocument;

    public QuotationService(QuotationRepository quotationRepository,
                            CompanyRepository companyRepository,
                            DocumentRepository documentRepository,
                            DocumentSerialRepository documentSerialRepository,
                            QuotationMapper quotationMapper,
                            QuotationDocument quotationDocument) {
        super(companyRepository, documentRepository, documentSerialRepository, 3);
        this.quotationRepository = quotationRepository;
        this.quotationMapper = quotationMapper;
        this.quotationDocument = quotationDocument;
    }

    public List<QuotationDto> findAllQuotations() {
        log.info("Find all quotations");
        return quotationRepository.findAll().stream()
                .map(quotationMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResultPage<QuotationDto> findAllQuotations(Integer page, Integer limit, Map<String, Object> params) {
        log.info("Find all quotations, page: {}, limit: {}, params: {}", page, limit, params);
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "documentId"));

        Page<Quotation> quotationPage = quotationRepository.findAll(filterByParams(params), pageable);
        List<QuotationDto> quotations = quotationPage.map(quotationMapper::toDto).toList();

        return ResultPage.of(quotations, page, limit, (int) quotationPage.getTotalElements());
    }

    public Optional<QuotationDto> findAllQuotationById(UUID id) {
        log.info("Find quotation by ID: {}", id);
        return quotationRepository.findById(id)
                .map(quotationMapper::toDto);
    }

    public Optional<QuotationDto> creteNewQuotation(QuotationDto newQuotationDto) {
        log.info("Create new quotation document ID: {}", newQuotationDto.getDocumentId());
        Quotation newQuotationEntity = quotationMapper.toEntity(newQuotationDto);
        newQuotationEntity.setStatus(QuotationStatus.SUBMITTED);

        newQuotationEntity.setLineItems(newQuotationEntity.getLineItems().stream()
                .map(quotationLineItem -> {
                    quotationLineItem.setQuotation(newQuotationEntity);
                    return quotationLineItem;
                }).collect(Collectors.toList()));

        Quotation savedQuotationEntity = quotationRepository.save(newQuotationEntity);
        updateDocumentId(savedQuotationEntity.getDocumentId());

        return Optional.of(savedQuotationEntity).map(quotationMapper::toDto);
    }

    public Optional<QuotationDto> updateQuotationById(UUID id, QuotationDto updateQuotation) {
        log.info("Update quotation by ID: {}", id);
        Quotation updateQuotationEntity = quotationMapper.toEntity(updateQuotation);
        return quotationRepository.findById(id)
                .map(quotation -> {
                    Quotation mappedQuotation = quotationMapper.update(quotation, updateQuotationEntity);
                    mappedQuotation.setLineItems(mappedQuotation.getLineItems().stream()
                            .map(quotationLineItem -> {
                                quotationLineItem.setQuotation(mappedQuotation);
                                return quotationLineItem;
                            }).collect(Collectors.toList()));
                    return quotationRepository.save(mappedQuotation);
                })
                .map(quotationMapper::toDto);
    }

    public void deleteQuotation(UUID id) {
        log.info("Delete quotation by ID: {}", id);
        quotationRepository.findById(id)
                .ifPresent(quotationRepository::delete);
    }

    public Optional<byte[]> generateDocumentById(UUID id, boolean isSignAndStamp) {
        return quotationRepository.findById(id)
                .flatMap(quotation -> {
                    try {
                        return quotationDocument.generate(
                                quotation.getCompany(),
                                DocumentType.FULL,
                                quotationMapper.toDocument(quotation),
                                isSignAndStamp
                        );
                    } catch (Exception e) {
                        log.error("Error: {}", e.getMessage(), e);
                    }
                    return Optional.empty();
                });
    }
}
