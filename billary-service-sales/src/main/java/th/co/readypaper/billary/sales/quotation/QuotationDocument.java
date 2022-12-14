package th.co.readypaper.billary.sales.quotation;

import org.springframework.stereotype.Component;
import th.co.readypaper.billary.sales.common.DocumentFactory;

import java.util.Collections;
import java.util.List;

@Component
public class QuotationDocument extends DocumentFactory {

    @Override
    public String getDocumentTitle() {
        return "ใบเสนอราคา";
    }

    @Override
    public String getDocumentShortTitle() {
        return "ใบเสนอราคา";
    }

    @Override
    public List<String> getDocumentCopies() {
        return Collections.singletonList("");
    }

    @Override
    public String getSignatureTitle() {
        return "ผู้สั่งซื้อ";
    }
}
