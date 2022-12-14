package th.co.readypaper.billary.sales.receipt;

import org.springframework.stereotype.Component;
import th.co.readypaper.billary.sales.common.DocumentFactory;

import java.util.Arrays;
import java.util.List;

@Component
public class ReceiptDocument extends DocumentFactory {
    @Override
    public String getDocumentTitle() {
        return "ใบเสร็จรับเงิน / ใบกำกับภาษี";
    }

    @Override
    public String getDocumentShortTitle() {
        return "ใบเสร็จรับเงิน/ใบกำกับภาษีอย่างย่อ";
    }

    @Override
    public List<String> getDocumentCopies() {
        return Arrays.asList("ต้นฉบับ", "สำเนา");
    }

    @Override
    public String getSignatureTitle() {
        return "ผู้ชำระเงิน";
    }
}
