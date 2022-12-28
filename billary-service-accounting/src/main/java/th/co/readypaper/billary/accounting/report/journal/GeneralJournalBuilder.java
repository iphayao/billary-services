package th.co.readypaper.billary.accounting.report.journal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.repo.entity.account.chart.AccountChart;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournal;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalCredit;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalDebit;
import th.co.readypaper.billary.repo.entity.invoice.Invoice;
import th.co.readypaper.billary.repo.repository.AccountChartRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static th.co.readypaper.billary.accounting.constant.Constants.*;

@Slf4j
@Component
public class GeneralJournalBuilder {
    private final AccountChartRepository accountChartRepository;

    public GeneralJournalBuilder(AccountChartRepository accountChartRepository) {
        this.accountChartRepository = accountChartRepository;
    }

    GeneralJournal build(Invoice invoice) {
        return GeneralJournal.builder()
                .reference(invoice.getId())
                .documentId(invoice.getDocumentId())
                .type("Invoice")
                .date(invoice.getIssuedDate())
                .debit(buildGeneralJournalDebit(invoice))
                .credit(buildGeneralJournalCredit(invoice))
                .build();
    }

    private List<GeneralJournalDebit> buildGeneralJournalDebit(Invoice invoice) {
        List<GeneralJournalDebit> debits = new ArrayList<>();
        debits.add(GeneralJournalDebit.builder() // ลูกหนี้การค้า
                .code(accountChartCodeOf(INVOICE_DEBIT_CODE))
                .desc(accountChartDescOf(INVOICE_DEBIT_CODE))
                .amount(amountOf(invoice.getTotal()))
                .build());
        return debits;
    }

    private List<GeneralJournalCredit> buildGeneralJournalCredit(Invoice invoice) {
        List<GeneralJournalCredit> credits = new ArrayList<>();
        if (invoice.getVatAmount() == null || invoice.getVatAmount().equals(BigDecimal.ZERO)) {
            // ไม่มี ภาษ๊มูลค่าเพิ่ม
            credits.add(GeneralJournalCredit.builder()
                    .code(accountChartCodeOf(INVOICE_VAT_SELL_CODE))
                    .desc(accountChartDescOf(INVOICE_VAT_SELL_CODE))
                    .amount(amountOf(invoice.getTotal()))
                    .build());
        } else {
            // มี ภาษ๊มูลค่าเพิ่ม
            credits.add(GeneralJournalCredit.builder()
                    .code(accountChartCodeOf(INVOICE_VAT_SELL_CODE))
                    .desc(accountChartDescOf(INVOICE_VAT_SELL_CODE))
                    .amount(amountOf(invoice.getTotal()))
                    .build());
            credits.add(GeneralJournalCredit.builder()
                    .code(accountChartCodeOf(INVOICE_CREDIT_VAT_CODE))
                    .desc(accountChartDescOf(INVOICE_CREDIT_VAT_CODE))
                    .amount(amountOf(invoice.getTotal()))
                    .build());
        }
        return credits;
    }

    private BigDecimal amountOf(BigDecimal amount) {
        if (amount != null) {
            return amount.setScale(2, RoundingMode.HALF_DOWN);
        }
        return BigDecimal.ZERO;
    }

    @Cacheable
    private Optional<AccountChart> getAccountChartByCode(String code) {
        return accountChartRepository.findByCode(INVOICE_DEBIT_CODE);
    }

    private String accountChartCodeOf(String code) {
        return getAccountChartByCode(code)
                .map(AccountChart::getCode)
                .orElse("");
    }

    private String accountChartDescOf(String code) {
        return getAccountChartByCode(code)
                .map(AccountChart::getName)
                .orElse("");
    }

}
