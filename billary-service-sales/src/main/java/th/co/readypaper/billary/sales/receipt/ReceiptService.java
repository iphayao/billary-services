package th.co.readypaper.billary.sales.receipt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.repo.entity.invoice.InvoiceStatus;
import th.co.readypaper.billary.repo.entity.receipt.Receipt;
import th.co.readypaper.billary.repo.entity.receipt.ReceiptStatus;
import th.co.readypaper.billary.repo.repository.*;
import th.co.readypaper.billary.common.service.DocumentIdBaseService;
import th.co.readypaper.billary.sales.common.model.document.DocumentDto;
import th.co.readypaper.billary.sales.common.model.document.DocumentType;
import th.co.readypaper.billary.sales.receipt.model.dto.ReceiptDto;
import th.co.readypaper.billary.sales.receipt.model.dto.ReceiptPaymentTypeDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReceiptService extends DocumentIdBaseService {
    private final ReceiptRepository receiptRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final ReceiptDocument receiptDocument;
    private final ReceiptMapper receiptMapper;

    public ReceiptService(ReceiptRepository receiptRepository,
                          CompanyRepository companyRepository,
                          DocumentRepository documentRepository,
                          DocumentSerialRepository documentSerialRepository,
                          InvoiceRepository invoiceRepository,
                          PaymentTypeRepository paymentTypeRepository,
                          ReceiptDocument receiptDocument,
                          ReceiptMapper receiptMapper) {
        super(companyRepository, documentRepository, documentSerialRepository, 2);
        this.receiptRepository = receiptRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentTypeRepository = paymentTypeRepository;
        this.receiptDocument = receiptDocument;
        this.receiptMapper = receiptMapper;
    }

    public ResultPage<ReceiptDto> findAllReceipts(Integer page, Integer limit, Map<String, Object> params) {
        log.info("Find all receipt, page: {}, limit: {}, params: {}", page, limit, params);
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "documentId"));

        Page<Receipt> receiptPage = receiptRepository.findAll(pageable);
        List<ReceiptDto> receipts = receiptPage.map(receiptMapper::toDto).toList();

        return ResultPage.of(receipts, page, limit, (int) receiptPage.getTotalElements());
    }

    public Optional<ReceiptDto> findReceiptById(UUID id) {
        log.info("Find receipt by ID: {}", id);
        return receiptRepository.findById(id)
                .map(receiptMapper::toDto);
    }

    public Optional<ReceiptDto> createNewReceipt(ReceiptDto newReceiptDto) {
        log.info("Create new receipt document ID: {}", newReceiptDto.getDocumentId());
        Receipt newReceiptEntity = receiptMapper.toEntity(newReceiptDto);
        if (newReceiptEntity.getStatus().equals(ReceiptStatus.DRAFT)) {
            newReceiptEntity.setStatus(ReceiptStatus.SUBMITTED);
        }

        if (newReceiptEntity.getReference() != null && !newReceiptEntity.getReference().isEmpty()) {
            invoiceRepository.findByDocumentId(newReceiptEntity.getReference())
                    .map(invoice -> {
                        log.info("Update invoice status for receipt ID: {}/{}", invoice.getDocumentId(), invoice.getId());
                        invoice.setStatus(InvoiceStatus.AUTHORIZED);
                        return invoice;
                    })
                    .ifPresent(invoiceRepository::save);
        }

        if (newReceiptEntity.getPayment() != null) {
            newReceiptEntity.getPayment().setReceipt(newReceiptEntity);
        }
        newReceiptEntity.setLineItems(newReceiptEntity.getLineItems().stream()
                .map(receiptLineItem -> {
                    receiptLineItem.setId(null);
                    receiptLineItem.setReceipt(newReceiptEntity);
                    return receiptLineItem;
                }).collect(Collectors.toList()));

        Receipt savedReceiptEntity = receiptRepository.save(newReceiptEntity);
        updateDocumentId(savedReceiptEntity.getDocumentId());

        return Optional.of(savedReceiptEntity).map(receiptMapper::toDto);
    }

    @Transactional
    public Optional<ReceiptDto> updateReceiptById(UUID id, ReceiptDto updateReceiptDto) {
        log.info("Update receipt by ID: {}", id);
        Receipt updateReceiptEntity = receiptMapper.toEntity(updateReceiptDto);
        return receiptRepository.findById(id)
                .map(receipt -> {
                    Receipt mappedReceipt = receiptMapper.update(receipt, updateReceiptEntity);
                    if (mappedReceipt.getPayment() != null) {
                        if(mappedReceipt.getPayment().getReceipt() == null) {
                            mappedReceipt.getPayment().setReceipt(mappedReceipt);
                        }
                        mappedReceipt.setStatus(ReceiptStatus.PAID);
                    } else {
                        mappedReceipt.setStatus(ReceiptStatus.SUBMITTED);
                    }
                    mappedReceipt.setLineItems(mappedReceipt.getLineItems().stream()
                            .peek(receiptLineItem -> receiptLineItem.setReceipt(mappedReceipt))
                            .collect(Collectors.toList()));

                    return receiptRepository.save(mappedReceipt);
                })
//                .map(receipt -> {
//                    entityManager.refresh(receipt);
//                    return receipt;
//                })
                .map(receiptMapper::toDto);
    }

    public void deleteReceiptById(UUID id) {
        log.info("Delete receipt by ID: {}", id);
        receiptRepository.findById(id)
                .ifPresent(receiptRepository::delete);
    }

    public Optional<byte[]> generateDocumentById(UUID id, DocumentType documentType, boolean isSignAndStamp) {
        log.info("Generate document by ID: {}", id);
        return receiptRepository.findById(id)
                .map(receipt -> {
                    log.info("Update receipt status for receipt ID: {}/{}", receipt.getDocumentId(), id);
                    receipt.setStatus(ReceiptStatus.AUTHORIZED);
                    return receiptRepository.save(receipt);
                })
                .flatMap(receipt -> invoiceRepository.findByDocumentId(receipt.getReference())
                        .flatMap(invoice -> {
                            try {
                                DocumentDto documentDto = receiptMapper.toDocument(receipt);
                                return receiptDocument.generate(
                                        invoice.getCompany(),
                                        documentType,
                                        documentDto,
                                        invoice.getDocumentId(),
                                        isSignAndStamp);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return Optional.empty();
                        })
                );
    }

    public List<ReceiptPaymentTypeDto> getPaymentType() {
        log.info("Get payment types");
        return paymentTypeRepository.findAll().stream()
                .map(receiptMapper::toDto)
                .collect(Collectors.toList());
    }

}
