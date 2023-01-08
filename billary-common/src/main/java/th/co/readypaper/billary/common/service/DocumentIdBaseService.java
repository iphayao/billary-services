package th.co.readypaper.billary.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import th.co.readypaper.billary.common.model.DocumentId;
import th.co.readypaper.billary.common.utils.DateUtils;
import th.co.readypaper.billary.repo.entity.billing.BillingNote;
import th.co.readypaper.billary.repo.entity.document.Document;
import th.co.readypaper.billary.repo.entity.document.DocumentSerial;
import th.co.readypaper.billary.repo.repository.CompanyRepository;
import th.co.readypaper.billary.repo.repository.DocumentRepository;
import th.co.readypaper.billary.repo.repository.DocumentSerialRepository;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.jpa.domain.Specification.where;


@Slf4j
public class DocumentIdBaseService<T> {
    private final CompanyRepository companyRepository;
    private final DocumentRepository documentRepository;
    private final DocumentSerialRepository documentSerialRepository;
    private final int documentType;

    public DocumentIdBaseService(CompanyRepository companyRepository,
                                 DocumentRepository documentRepository,
                                 DocumentSerialRepository documentSerialRepository,
                                 int documentType) {
        this.companyRepository = companyRepository;
        this.documentRepository = documentRepository;
        this.documentSerialRepository = documentSerialRepository;
        this.documentType = documentType;
    }

    public Optional<DocumentId> generateDocumentId(LocalDate publishedOn) {
        log.info("{}", publishedOn);
        int docMonth = DateUtils.yearMonthOf(publishedOn).getMonthValue();
        int docYear = DateUtils.yearMonthOf(publishedOn).getYear();

        Optional<Document> document = companyRepository.findByCode("T000000001")
                .flatMap(company -> documentRepository.findByCompanyIdAndDocumentTypeId(company.getId(), documentType));

        if (document.isPresent()) {
            Document doc = document.get();
            Optional<DocumentSerial> documentSerial = doc.getDocumentSerials().stream()
                    .filter(ds -> ds.getCurrentYear() == docYear && ds.getCurrentMonth() == docMonth)
                    .findFirst();

            DocumentSerial ds;
            if (documentSerial.isPresent()) {
                ds = documentSerial.get();
            } else {
                ds = buildDocumentSerial(doc.getId(), docMonth, docYear);
                documentSerialRepository.save(ds);
            }
            return DocumentId.of(DateUtils.generate(doc.getDocumentType().getPrefix(),
                    ds.getCurrentYear(), ds.getCurrentMonth(), ds.getCurrentNumber()));
        }

        return DocumentId.of("ERROR");

    }

    public void updateDocumentId(String documentId) {
        log.info("{}", documentId);
        int docMonth = DateUtils.dateOf(documentId).getMonthValue();
        int docYear = DateUtils.dateOf(documentId).getYear();

        Optional<Document> document = companyRepository.findByCode("T000000001")
                .flatMap(company -> documentRepository.findByCompanyIdAndDocumentTypeId(company.getId(), documentType));

        if (document.isPresent()) {
            Document doc = document.get();
            Optional<DocumentSerial> documentSerial = doc.getDocumentSerials().stream()
                    .filter(ds -> ds.getCurrentYear() == docYear && ds.getCurrentMonth() == docMonth)
                    .findFirst();

            DocumentSerial ds;
            if (documentSerial.isPresent()) {
                ds = documentSerial.get();
                ds.setCurrentNumber(ds.getCurrentNumber() + 1);
                documentSerialRepository.save(ds);
            }
        }
    }

    private DocumentSerial buildDocumentSerial(UUID documentId, int docMonth, int docYear) {
        DocumentSerial ds = new DocumentSerial();
        ds.setDocumentId(documentId);
        ds.setCurrentNumber(1);
        ds.setCurrentMonth(docMonth);
        ds.setCurrentYear(docYear);
        return ds;
    }

    protected Specification<T> filterByParams(Map<String, Object> params) {
        return where(hasKeyValue("documentId", params.get("documentId")))
                .and(hasKeyValue("contact", params.get("contact")))
                .and(hasKeyValue("saleChannel", params.get("saleChannel")));
    }

    protected Specification<T> hasKeyValue(String key, Object contact) {
        if (contact != null) {
            return (entity, cq, cb) -> cb.like(entity.get(key).get("name"), "%" + contact + "%");
        }
        return null;
    }

    protected Specification<T> hasContactName(Object contact) {
        if (contact != null) {
            return (entity, cq, cb) -> cb.like(entity.get("contact").get("name"), "%" + contact + "%");
        }
        return null;
    }

    protected Specification<T> hasDocumentId(Object documentId) {
        if (documentId != null) {
            return (entity, cq, cb) -> cb.like(entity.get("documentId"), "%" + documentId + "%");
        }
        return null;
    }

}
