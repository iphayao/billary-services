package th.co.readypaper.billary.accounting.report.journal;

import th.co.readypaper.billary.repo.entity.invoice.Invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GeneralJournalHelper {

    public static BigDecimal amountOf(BigDecimal amount) {
        if (amount != null) {
            return amount.setScale(2, RoundingMode.HALF_DOWN);
        }
        return BigDecimal.ZERO;
    }

    public static String descOf(Invoice invoice) {
        return "ขายสินค้าให้กับ " + invoice.getContact().getName() + " (" + invoice.getSaleChannel() + ")";
    }

}
