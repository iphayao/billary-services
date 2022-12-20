package th.co.readypaper.billary.sales.invoice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.repo.repository.InvoiceRepository;
import th.co.readypaper.billary.repo.entity.invoice.Invoice;
import th.co.readypaper.billary.repo.entity.invoice.InvoiceStatus;
import th.co.readypaper.billary.repo.repository.CompanyRepository;
import th.co.readypaper.billary.repo.repository.BillingNoteRepository;
import th.co.readypaper.billary.common.service.DocumentIdBaseService;
import th.co.readypaper.billary.sales.common.model.document.DocumentType;
import th.co.readypaper.billary.repo.repository.DocumentRepository;
import th.co.readypaper.billary.repo.repository.DocumentSerialRepository;
import th.co.readypaper.billary.sales.invoice.model.dto.InvoiceDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

@Slf4j
@Service
public class InvoiceService extends DocumentIdBaseService<Invoice> {
    private final InvoiceRepository invoiceRepository;
    private final BillingNoteRepository billingNoteRepository;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceDocument invoiceDocument;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          CompanyRepository companyRepository,
                          DocumentRepository documentRepository,
                          DocumentSerialRepository documentSerialRepository,
                          BillingNoteRepository billingNoteRepository,
                          InvoiceMapper invoiceMapper,
                          InvoiceDocument invoiceDocument) {
        super(companyRepository, documentRepository, documentSerialRepository, 1);
        this.invoiceRepository = invoiceRepository;
        this.billingNoteRepository = billingNoteRepository;
        this.invoiceMapper = invoiceMapper;
        this.invoiceDocument = invoiceDocument;
    }

    public List<InvoiceDto> findAllInvoices() {
        log.info("Find all invoices");
        return invoiceRepository.findAll().stream()
                .map(invoiceMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResultPage<InvoiceDto> findAllInvoices(Integer page, Integer limit, Map<String, Object> params) {
        log.info("Find all invoice, page: {}, limit: {}, params: {}", page, limit, page);
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "documentId"));

        Page<Invoice> invoicePage = invoiceRepository.findAll(filterByParams(params), pageable);
        List<InvoiceDto> invoices = invoicePage.map(invoiceMapper::toDto).toList();

        return ResultPage.of(invoices, page, limit, (int) invoicePage.getTotalElements());
    }

    public Optional<InvoiceDto> findInvoiceById(UUID id) {
        log.info("Find invoice by ID: {}", id);
        return invoiceRepository.findById(id)
                .map(invoiceMapper::toDto);
    }

    public Optional<InvoiceDto> createNewInvoice(InvoiceDto newInvoiceDto) {
        log.info("Create new invoice document ID: {}", newInvoiceDto.getDocumentId());
        Invoice newInvoiceEntity = invoiceMapper.toEntity(newInvoiceDto);
        if (newInvoiceEntity.getStatus().equals(InvoiceStatus.DRAFT)) {
            newInvoiceEntity.setStatus(InvoiceStatus.SUBMITTED);
        }

        newInvoiceEntity.setLineItems(newInvoiceEntity.getLineItems().stream()
                .map(invoiceLineItem -> {
                    invoiceLineItem.setInvoice(newInvoiceEntity);
                    return invoiceLineItem;
                }).collect(Collectors.toList()));

        Invoice savedInvoiceEntity = invoiceRepository.save(newInvoiceEntity);
        updateDocumentId(savedInvoiceEntity.getDocumentId());

        assert savedInvoiceEntity.getId() != null;
        return invoiceRepository.findById(savedInvoiceEntity.getId())
                .map(invoiceMapper::toDto);
    }

    public Optional<InvoiceDto> updateInvoiceById(UUID id, InvoiceDto updateInvoiceDto) {
        log.info("Update invoice by ID: {}", id);
        Invoice updateInvoiceEntity = invoiceMapper.toEntity(updateInvoiceDto);
        return invoiceRepository.findById(id)
                .map(invoice -> {
                    Invoice mappedInvoice = invoiceMapper.update(invoice, updateInvoiceEntity);
                    mappedInvoice.setLineItems(mappedInvoice.getLineItems().stream()
                            .map(invoiceLineItem -> {
                                invoiceLineItem.setInvoice(mappedInvoice);
                                return invoiceLineItem;
                            }).collect(Collectors.toList()));
                    return invoiceRepository.save(mappedInvoice);
                })
                .map(invoiceMapper::toDto);

    }

    public void deleteInvoiceById(UUID id) {
        log.info("Delete invoice by ID: {}", id);
        invoiceRepository.findById(id)
                .ifPresent(invoiceRepository::delete);
    }

    public Optional<byte[]> generateDocumentById(UUID id, DocumentType documentType, boolean isSignAndStamp, boolean isDeliveryNoteAndInvoice) {
        log.info("Generate document by ID: {}", id);
        return invoiceRepository.findById(id)
                .flatMap(invoice -> {
                            try {
                                return invoiceDocument.generate(
                                        invoice.getCompany(),
                                        documentType,
                                        invoiceMapper.toDocument(invoice),
                                        invoice.getReference(),
                                        true,
                                        isDeliveryNoteAndInvoice
                                );
                            } catch (Exception e) {
                                log.error("Error: {}", e.getMessage(), e);
                            }
                            return Optional.empty();
                        }
                );
    }
}
