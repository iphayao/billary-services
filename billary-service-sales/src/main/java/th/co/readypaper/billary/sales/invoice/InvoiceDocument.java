package th.co.readypaper.billary.sales.invoice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.repo.entity.company.Company;
import th.co.readypaper.billary.sales.common.DocumentFactory;
import th.co.readypaper.billary.sales.common.model.document.DocumentDto;
import th.co.readypaper.billary.sales.common.model.document.DocumentType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class InvoiceDocument extends DocumentFactory {
    private static final String TITLE_DELIVERY_NOTE = "ใบแจ้งหนี้";
    private static final String TITLE_INVOICE = "ใบกำกับภาษี";

    private String documentTitle;

    public Optional<byte[]> generate(Company company, DocumentType docType, DocumentDto documentDto, String referenceId,
                                     boolean isSignAndStamp, boolean isDeliveryNoteAndInvoice)
            throws IOException {

        generateDocumentTitle(isDeliveryNoteAndInvoice);

        return super.generate(company, docType, documentDto, referenceId, isSignAndStamp);
    }

    public Optional<byte[]> generate(Company company, DocumentType docType, DocumentDto documentDto,
                                 boolean isSignAndStamp, boolean isDeliveryNoteAndInvoice)
            throws IOException {

        generateDocumentTitle(isDeliveryNoteAndInvoice);

        return super.generate(company, docType, documentDto, isSignAndStamp);
    }


    private void generateDocumentTitle(boolean isDeliveryNoteAndInvoice) {
        if (isDeliveryNoteAndInvoice) {
            documentTitle = TITLE_DELIVERY_NOTE + " / " + TITLE_INVOICE;
        } else {
            documentTitle = TITLE_DELIVERY_NOTE;
        }
    }

    @Override
    public String getDocumentTitle() {
        log.info("Document Title: {}", documentTitle);
        return documentTitle;
    }

    @Override
    public String getDocumentShortTitle() {
        return "ใบกำกับภาษีอย่างย่อ";
    }

    @Override
    public List<String> getDocumentCopies() {
        return Arrays.asList("ต้นฉบับ", "สำเนา");
    }

    @Override
    public String getSignatureTitle() {
        return "ผู้รับสินค้า";
    }
}
