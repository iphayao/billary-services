package th.co.readypaper.billary.sales.billing;

import org.springframework.stereotype.Component;
import th.co.readypaper.billary.sales.common.DocumentFactory;

import java.util.Arrays;
import java.util.List;

@Component
public class BillingNoteDocument extends DocumentFactory {

    @Override
    public String getDocumentTitle() {
        return "ใบวางบิล";
    }

    @Override
    public String getDocumentShortTitle() {
        return "ใบวางบิล";
    }

    @Override
    public List<String> getDocumentCopies() {
        return Arrays.asList("ต้นฉบับ", "สำเนา");
    }

    @Override
    public String getSignatureTitle() {
        return "ผู้รับวางบิล";
    }
}
