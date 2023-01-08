package th.co.readypaper.billary.accounting.report.journal.builder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.repo.entity.account.chart.AccountChart;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournal;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalCredit;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalDebit;
import th.co.readypaper.billary.repo.entity.invoice.Invoice;
import th.co.readypaper.billary.repo.repository.AccountChartRepository;
import th.co.readypaper.billary.repo.repository.InvoiceRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static th.co.readypaper.billary.accounting.common.Constants.*;
import static th.co.readypaper.billary.accounting.common.Constants.INVOICE_CREDIT_VAT_CODE;
import static th.co.readypaper.billary.accounting.report.journal.GeneralJournalHelper.amountOf;
import static th.co.readypaper.billary.accounting.report.journal.GeneralJournalHelper.descOf;

@Slf4j
@Component
public class GeneralJournalInvoiceBuilder {
    private final AccountChartRepository accountChartRepository;
    private final InvoiceRepository invoiceRepository;

    public GeneralJournalInvoiceBuilder(AccountChartRepository accountChartRepository,
                                        InvoiceRepository invoiceRepository) {
        this.accountChartRepository = accountChartRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public List<GeneralJournal> build(LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.findByIssuedDateBetween(startDate, endDate)
                .stream().parallel()
                .map(this::buildGeneralJournal)
                .collect(Collectors.toList());
    }

    GeneralJournal buildGeneralJournal(Invoice invoice) {
        log.info("Invoice DocumentID: {}", invoice.getDocumentId());
        var generalJournal = GeneralJournal.builder()
                .reference(invoice.getId())
                .referenceDate(invoice.getIssuedDate())
                .documentId(invoice.getDocumentId())
                .description(descOf(invoice))
                .type("Invoice")
                .date(invoice.getIssuedDate())
                .debits(buildGeneralJournalDebit(invoice))
                .credits(buildGeneralJournalCredit(invoice))
                .build();

        generalJournal.setDebits(generalJournal.getDebits().stream()
                .peek(debit -> debit.setGeneralJournal(generalJournal))
                .toList());
        generalJournal.setCredits(generalJournal.getCredits().stream()
                .peek(credit -> credit.setGeneralJournal(generalJournal))
                .toList());

        return generalJournal;
    }

    private List<GeneralJournalDebit> buildGeneralJournalDebit(Invoice invoice) {
        var debits = new ArrayList<GeneralJournalDebit>();
        debits.add(GeneralJournalDebit.builder() // ลูกหนี้การค้า
                .code(accountChartCodeOf(INVOICE_DEBIT_CODE))
                .desc(accountChartDescOf(INVOICE_DEBIT_CODE))
                .amount(amountOf(invoice.getTotal()))
                .build());
        return debits;
    }

    private List<GeneralJournalCredit> buildGeneralJournalCredit(Invoice invoice) {
        var credits = new ArrayList<GeneralJournalCredit>();
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
                    .amount(amountOf(invoice.getVatableAmount()))
                    .build());
            credits.add(GeneralJournalCredit.builder()
                    .code(accountChartCodeOf(INVOICE_CREDIT_VAT_CODE))
                    .desc(accountChartDescOf(INVOICE_CREDIT_VAT_CODE))
                    .amount(amountOf(invoice.getVatAmount()))
                    .build());
        }
        return credits;
    }

    @Cacheable
    private Optional<AccountChart> getAccountChartByCode(String code) {
        return accountChartRepository.findByCode(code);
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
