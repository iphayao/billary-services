package th.co.readypaper.billary.accounting.report.journal.builder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.repo.entity.account.chart.AccountChart;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournal;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalCredit;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalDebit;
import th.co.readypaper.billary.repo.entity.expense.Expense;
import th.co.readypaper.billary.repo.entity.expense.ExpenseLineItem;
import th.co.readypaper.billary.repo.repository.AccountChartRepository;
import th.co.readypaper.billary.repo.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static th.co.readypaper.billary.accounting.common.Constants.*;
import static th.co.readypaper.billary.accounting.report.journal.GeneralJournalHelper.amountOf;

@Slf4j
@Component
public class GeneralJournalExpenseBuilder {
    private final AccountChartRepository accountChartRepository;
    private final ExpenseRepository expenseRepository;

    public GeneralJournalExpenseBuilder(AccountChartRepository accountChartRepository,
                                        ExpenseRepository expenseRepository) {
        this.accountChartRepository = accountChartRepository;
        this.expenseRepository = expenseRepository;
    }

    public List<GeneralJournal> build(LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByIssuedDateBetween(startDate, endDate)
                .stream().parallel()
                .map(this::buildGeneralJournal)
                .collect(Collectors.toList());
    }

    private GeneralJournal buildGeneralJournal(Expense expense) {
        log.info("Expenses DocumentID: {}", expense.getDocumentId());
        var generalJournal = GeneralJournal.builder()
                .reference(expense.getId())
                .documentId(expense.getDocumentId())
                .description(descOf(expense))
                .type("Expense")
                .date(expense.getIssuedDate())
                .debits(buildGeneralJournalDebit(expense))
                .credits(buildGeneralJournalCredit(expense))
                .build();

        generalJournal.setDebits(generalJournal.getDebits().stream()
                .peek(debit -> debit.setGeneralJournal(generalJournal))
                .toList());
        generalJournal.setCredits(generalJournal.getCredits().stream()
                .peek(credit -> credit.setGeneralJournal(generalJournal))
                .toList());
        return generalJournal;
    }

    private List<GeneralJournalDebit> buildGeneralJournalDebit(Expense expense) {
        var debits = new ArrayList<GeneralJournalDebit>();

        expenseGroupOf(expense.getLineItems()).forEach(expenseLineItem -> {
            if (expenseLineItem.getAccountChart() != null) {
                String accountCode = expenseLineItem.getAccountChart().getCode();
                if (expenseLineItem.getVatAmount().equals(BigDecimal.ZERO) || !expense.isUseInputTax()) {
                    // ไม่มี vat และ ไม่ใช้ภาษีซื้อ
                    debits.add(GeneralJournalDebit.builder()
                            .code(expenseLineItem.getAccountChart().getCode())
                            .desc(accountChartDescOf(accountCode))
                            .amount(amountOf(expenseLineItem.getLineAmount()))
                            .build());
                } else {
                    // มี vat หรือ ใช้ภาษีซื้อ
                    debits.add(GeneralJournalDebit.builder()
                            .code(expenseLineItem.getAccountChart().getCode())
                            .desc(accountChartDescOf(accountCode))
                            .amount(amountOf(expenseLineItem.getVatableAmount()))
                            .build());
                    debits.add(GeneralJournalDebit.builder()
                            .code(accountChartCodeOf(EXPENSE_VAT_BUY_CODE))
                            .desc(accountChartDescOf(EXPENSE_VAT_BUY_CODE))
                            .amount(amountOf(expenseLineItem.getVatAmount()))
                            .build());
                }
            } else {
                log.error("Document ID: {}", expense.getDocumentId());
            }
        });

        return debits;
    }

    private List<ExpenseLineItem> expenseGroupOf(List<ExpenseLineItem> lineItems) {
        Map<String, ExpenseLineItem> groups = new HashMap<>();

        lineItems.forEach(lineItem -> {
            if (lineItem.getAccountChart() != null) {
                String key = lineItem.getAccountChart().getCode();
                ExpenseLineItem expenseLineItem;
                if (groups.containsKey(key)) {
                    expenseLineItem = groups.get(key);
                    expenseLineItem.setAccountChart(lineItem.getAccountChart());
                    expenseLineItem.setDescription(expenseLineItem.getDescription().concat(", ").concat(lineItem.getDescription()));
                    expenseLineItem.setQuantity(expenseLineItem.getQuantity() + lineItem.getQuantity());
                    expenseLineItem.setUnitPrice(expenseLineItem.getUnitPrice().add(lineItem.getUnitPrice()));
                    expenseLineItem.setTotalAmount(expenseLineItem.getTotalAmount().add(lineItem.getTotalAmount()));
                    expenseLineItem.setDiscountAmount(expenseLineItem.getDiscountAmount().add(lineItem.getDiscountAmount()));
                    expenseLineItem.setExemptVatAmount(expenseLineItem.getExemptVatAmount().add(lineItem.getExemptVatAmount()));
                    expenseLineItem.setVatableAmount(expenseLineItem.getVatableAmount().add(lineItem.getExemptVatAmount()));
                    expenseLineItem.setVatAmount(expenseLineItem.getVatAmount().add(lineItem.getVatAmount()));
                    expenseLineItem.setLineAmount(expenseLineItem.getLineAmount().add(lineItem.getLineAmount()));
                } else {
                    expenseLineItem = groups.get(key);
                    expenseLineItem.setAccountChart(lineItem.getAccountChart());
                    expenseLineItem.setDescription(lineItem.getDescription());
                    expenseLineItem.setQuantity(lineItem.getQuantity());
                    expenseLineItem.setUnitPrice(lineItem.getUnitPrice());
                    expenseLineItem.setTotalAmount(lineItem.getTotalAmount());
                    expenseLineItem.setDiscountAmount(lineItem.getDiscountAmount());
                    expenseLineItem.setExemptVatAmount(lineItem.getExemptVatAmount());
                    expenseLineItem.setVatableAmount(lineItem.getExemptVatAmount());
                    expenseLineItem.setVatAmount(lineItem.getVatAmount());
                    expenseLineItem.setLineAmount(lineItem.getLineAmount());
                }
                groups.put(key, expenseLineItem);
            }
        });

        return new ArrayList<>(groups.values());
    }

    private List<GeneralJournalCredit> buildGeneralJournalCredit(Expense expense) {
        var credits = new ArrayList<GeneralJournalCredit>();
        String accountCode;
        String accountDesc;

        if (expense.getPayment() != null
                && expense.getPayment().getPaymentType() != null
                && expense.getPayment().getPaymentType().getId() != null) {
            switch (expense.getPayment().getPaymentType().getId()) {
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
                case 6 -> {
                    accountCode = accountChartCodeOf(EXPENSE_ADVANCE_CODE);
                    accountDesc = accountChartDescOf(EXPENSE_ADVANCE_CODE);
                }
                default -> {
                    accountCode = accountChartCodeOf(EXPENSE_CASH_CODE);
                    accountDesc = accountChartDescOf(EXPENSE_CASH_CODE);
                }
            }

            credits.add(GeneralJournalCredit.builder()
                    .code(accountCode)
                    .desc(accountDesc)
                    .amount(amountOf(expense.getPaymentAmount()))
                    .build());

            if (expense.getWithholdingTaxAmount() != null &&
                    expense.getWithholdingTaxAmount().compareTo(BigDecimal.ZERO) > 0) {
                credits.add(GeneralJournalCredit.builder()
                        .code(accountChartCodeOf(WITHHOLDING_TAX_CODE))
                        .desc(accountChartDescOf(WITHHOLDING_TAX_CODE))
                        .amount(amountOf(expense.getWithholdingTaxAmount()))
                        .build());
            }
        }

        return credits;
    }

    private String descOf(Expense expense) {
        String desc = expense.getLineItems().stream()
                .map(ExpenseLineItem::getDescription)
                .collect(Collectors.joining(" "));

        if (!desc.startsWith("ค่า")) {
            desc = "ค่า" + desc;
        }

        return "ชำระ" + desc + " ให้กับ " + expense.getContact().getName();
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
