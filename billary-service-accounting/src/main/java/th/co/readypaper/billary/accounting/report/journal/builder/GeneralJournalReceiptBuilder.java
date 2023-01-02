package th.co.readypaper.billary.accounting.report.journal.builder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.repo.entity.account.chart.AccountChart;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournal;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalCredit;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalDebit;
import th.co.readypaper.billary.repo.entity.receipt.Receipt;
import th.co.readypaper.billary.repo.entity.receipt.ReceiptStatus;
import th.co.readypaper.billary.repo.repository.AccountChartRepository;
import th.co.readypaper.billary.repo.repository.ReceiptRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static th.co.readypaper.billary.accounting.common.Constants.*;
import static th.co.readypaper.billary.accounting.report.journal.GeneralJournalHelper.amountOf;

@Slf4j
@Component
public class GeneralJournalReceiptBuilder {
    private final AccountChartRepository accountChartRepository;
    private final ReceiptRepository receiptRepository;

    public GeneralJournalReceiptBuilder(AccountChartRepository accountChartRepository,
                                        ReceiptRepository receiptRepository) {
        this.accountChartRepository = accountChartRepository;
        this.receiptRepository = receiptRepository;
    }

    public List<GeneralJournal> build(LocalDate startDate, LocalDate endDate) {
        return receiptRepository.findByIssuedDateBetween(startDate, endDate)
                .parallelStream().parallel()
                .filter(receipt -> receipt.getStatus().equals(ReceiptStatus.PAID) && receipt.getPayment() != null)
                .map(this::buildGeneralJournal)
                .collect(Collectors.toList());
    }

    private GeneralJournal buildGeneralJournal(Receipt receipt) {
        log.info("Receipt DocumentID: {}", receipt.getDocumentId());
        var generalJournal = GeneralJournal.builder()
                .reference(receipt.getId())
                .documentId(receipt.getDocumentId())
                .description(descOf(receipt))
                .type("Receipt")
                .date(receipt.getIssuedDate())
                .debits(buildGeneralJournalDebit(receipt))
                .credits(buildGeneralJournalCredit(receipt))
                .build();

        generalJournal.setDebits(generalJournal.getDebits().stream()
                .peek(debit -> debit.setGeneralJournal(generalJournal))
                .toList());
        generalJournal.setCredits(generalJournal.getCredits().stream()
                .peek(credit -> credit.setGeneralJournal(generalJournal))
                .toList());

        return generalJournal;
    }

    private String descOf(Receipt receipt) {
        return "รับชำระจาก " + receipt.getContact().getName();
    }

    private List<GeneralJournalDebit> buildGeneralJournalDebit(Receipt receipt) {
        var debits = new ArrayList<GeneralJournalDebit>();
        String accountCode;
        String accountDesc;

        if (receipt.getPayment() != null && receipt.getPayment().getPaymentType() != null && receipt.getPayment().getPaymentType().getId() != null) {
            switch (receipt.getPayment().getPaymentType().getId()) {
                case 2, 3 -> {
                    accountCode = accountChartCodeOf(EXPENSE_BANK_ACC_KBANK_CODE);
                    accountDesc = accountChartDescOf(EXPENSE_BANK_ACC_KBANK_CODE);
                }
                case 4 -> {
                    accountCode = accountChartCodeOf(EXPENSE_BANK_ACC_SCB_CODE);
                    accountDesc = accountChartDescOf(EXPENSE_BANK_ACC_SCB_CODE);
                }
                case 5 -> {
                    accountCode = accountChartCodeOf(EXPENSE_CREDIT_CODE);
                    accountDesc = accountChartDescOf(EXPENSE_CREDIT_CODE);
                }
                default -> {
                    accountCode = accountChartCodeOf(EXPENSE_CASH_CODE);
                    accountDesc = accountChartDescOf(EXPENSE_CASH_CODE);
                }
            }

            debits.add(GeneralJournalDebit.builder()
                    .code(accountCode)
                    .desc(accountDesc)
                    .amount(amountOf(receipt.getTotal()))
                    .build());
        }

        return debits;
    }

    private List<GeneralJournalCredit> buildGeneralJournalCredit(Receipt receipt) {
        var credits = new ArrayList<GeneralJournalCredit>();
        credits.add(GeneralJournalCredit.builder()
                .code(accountChartCodeOf(RECEIPT_CREDIT_CODE))
                .desc(accountChartDescOf(RECEIPT_CREDIT_CODE))
                .amount(amountOf(receipt.getTotal()))
                .build());

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
