package th.co.readypaper.billary.accounting.report.ledger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournal;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalCredit;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalDebit;
import th.co.readypaper.billary.repo.entity.account.ledger.Ledger;
import th.co.readypaper.billary.repo.entity.account.ledger.LedgerCredit;
import th.co.readypaper.billary.repo.entity.account.ledger.LedgerDebit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static th.co.readypaper.billary.accounting.common.Constants.EXPENSE_VAT_BUY_CODE;
import static th.co.readypaper.billary.accounting.common.Constants.WITHHOLDING_TAX_CODE;

@Slf4j
@Component
public class LedgerBuilder {
    public void build(Integer year, Integer month, Map<String, Ledger> ledgers, GeneralJournal generalJournal) {
        log.info("{}", generalJournal.getDocumentId());
        // สร้างบัญชีแยกประเภท เดบิต
        generalJournal.getDebits()
                .forEach(generalJournalDebit -> {
                    String code = generalJournalDebit.getCode();

                    List<LedgerDebit> debits = new ArrayList<>();

                    if (isCreditContainWithHoldingTax(generalJournal)) {
                        if (generalJournal.getDebits().size() < 2) {
                            generalJournal.getCredits()
                                    .forEach(generalJournalCredit -> {
                                        debits.add(buildLedgerDebit(generalJournal, generalJournalCredit, generalJournalCredit.getAmount()));
                                    });
                        } else {
                            BigDecimal debitTotalAmount = generalJournal.getDebits().stream()
                                    .map(GeneralJournalDebit::getAmount)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            BigDecimal debitRatio = generalJournalDebit.getAmount()
                                    .divide(debitTotalAmount, 8, RoundingMode.HALF_DOWN);
                            generalJournal.getCredits()
                                    .forEach(generalJournalCredit -> {
                                        BigDecimal debitAmount = generalJournalCredit.getAmount().multiply(debitRatio);
                                        debits.add(buildLedgerDebit(generalJournal, generalJournalCredit, debitAmount));
                                    });
                        }
                    } else {
                        generalJournal.getCredits()
                                .forEach(generalJournalCredit -> {
                                    BigDecimal debitAmount = generalJournal.getCredits().size() > 1
                                            ? generalJournalCredit.getAmount()
                                            : generalJournalDebit.getAmount();
                                    debits.add(buildLedgerDebit(generalJournal, generalJournalCredit, debitAmount));
                                });
                    }

                    Ledger ledger;
                    if (ledgers.containsKey(code)) {
                        // เพิ่มไปในบัญชีแยกประเภทที่มีอยู่
                        ledger = ledgers.get(code);
                        ledger.getDebits().addAll(debits);
                        ledger.setSumDebit(sumOfDebit(ledgers.get(code).getDebits()));
                        ledger.setDiffSumDebitCredit(diffSumDebitCreditOf(ledgers.get(code)));
                    } else {
                        // สร้างบัญชีแยกประเภทใหม่
                        ledger = new Ledger();
                        ledger.setYear(year);
                        ledger.setMonth(month);
                        ledger.setCode(code);
                        ledger.setDesc(generalJournalDebit.getDesc());
                        ledger.setDebits(debits);
                        ledger.setCredits(new ArrayList<>());
                        ledger.setSumDebit(sumOfDebit(ledger.getDebits()));
                        ledger.setSumCredit(sumOfCredit(ledger.getCredits()));
                    }

                    ledger.setDebits(ledger.getDebits().stream()
                            .map(ledgerDebit -> {
                                ledgerDebit.setLedger(ledger);
                                return ledgerDebit;
                            }).collect(Collectors.toList()));
                    ledger.setCredits(ledger.getCredits().stream()
                            .map(ledgerCredit -> {
                                ledgerCredit.setLedger(ledger);
                                return ledgerCredit;
                            }).collect(Collectors.toList()));

                    ledgers.put(code, ledger);
                });

        // สร้างบัญชีแยกประเภท เครดิต
        generalJournal.getCredits()
                .forEach(generalJournalCredit -> {
                    String code = generalJournalCredit.getCode();

                    List<LedgerCredit> credits = new ArrayList<>();

                    if (isWithHoldingTax(code)) {
                        if (generalJournal.getDebits().size() > 2) {
                            generalJournal.getDebits()
                                    .forEach(generalJournalDebit -> {
                                        if (!isExpenseWithVat(generalJournalDebit.getCode())) {
                                            credits.add(buildLedgerCredit(generalJournal, generalJournalDebit, generalJournalCredit.getAmount()));
                                        }
                                    });
                        } else {
                            BigDecimal debitTotalAmount = generalJournal.getDebits().stream()
                                    .map(GeneralJournalDebit::getAmount)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            generalJournal.getDebits()
                                    .forEach(generalJournalDebit -> {
                                        if (!isExpenseWithVat(generalJournalDebit.getCode())) {
                                            credits.add(buildLedgerCredit(generalJournal, generalJournalDebit, generalJournalDebit.getAmount()
                                                    .divide(debitTotalAmount, RoundingMode.HALF_DOWN)
                                                    .multiply(generalJournalCredit.getAmount())));
                                        }
                                    });
                        }
                    } else {
                        if (generalJournal.getDebits().size() < 2) {
                            generalJournal.getDebits()
                                    .forEach(generalJournalDebit -> {
                                        if (!isCreditContainWithHoldingTax(generalJournal)
                                                && !isExpenseWithVat(generalJournalDebit.getCode())) {
                                            // รายการค่าใช้จ่ายที่ต้องหัก ภาษีหัก ณ ที่จ่ายออก
                                            credits.add(buildLedgerCredit(generalJournal, generalJournalDebit, generalJournalDebit.getAmount()));
                                        } else {
                                            BigDecimal creditAmount = (generalJournal.getDebits().size() > 1)
                                                    ? generalJournalDebit.getAmount()
                                                    : generalJournalCredit.getAmount();
                                            credits.add(buildLedgerCredit(generalJournal, generalJournalDebit, creditAmount));
                                        }
                                    });
                        } else {
                            BigDecimal debitTotalAmount = generalJournal.getDebits().stream()
                                    .map(GeneralJournalDebit::getAmount)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            generalJournal.getDebits()
                                    .forEach(generalJournalDebit -> {
                                        BigDecimal debitRatio = generalJournalDebit.getAmount()
                                                .divide(debitTotalAmount, 8, RoundingMode.HALF_DOWN);
                                        BigDecimal creditAmount = generalJournalCredit.getAmount().multiply(debitRatio);
                                        credits.add(buildLedgerCredit(generalJournal, generalJournalDebit, creditAmount));
                                    });
                        }

                        Ledger ledger;
                        if (ledgers.containsKey(code)) {
                            // เพิ่มไปในบัญชีแยกประเภทที่มีอยู่
                            ledger = ledgers.get(code);
                            ledger.getCredits().addAll(credits);
                            ledger.setSumDebit(sumOfDebit(ledgers.get(code).getDebits()));
                            ledger.setDiffSumDebitCredit(diffSumDebitCreditOf(ledgers.get(code)));
                        } else {
                            // สร้างบัญชีแยกประเภทใหม่
                            ledger = new Ledger();
                            ledger.setYear(year);
                            ledger.setMonth(month);
                            ledger.setCode(code);
                            ledger.setDesc(generalJournalCredit.getDesc());
                            ledger.setDebits(new ArrayList<>());
                            ledger.setCredits(credits);
                            ledger.setSumDebit(sumOfDebit(ledger.getDebits()));
                            ledger.setSumCredit(sumOfCredit(ledger.getCredits()));
                        }

                        ledger.setDebits(ledger.getDebits().stream()
                                .map(ledgerDebit -> {
                                    ledgerDebit.setLedger(ledger);
                                    return ledgerDebit;
                                }).collect(Collectors.toList()));
                        ledger.setCredits(ledger.getCredits().stream()
                                .map(ledgerCredit -> {
                                    ledgerCredit.setLedger(ledger);
                                    return ledgerCredit;
                                }).collect(Collectors.toList()));

                        ledgers.put(code, ledger);
                    }
                });
    }

    private static LedgerDebit buildLedgerDebit(GeneralJournal generalJournal, GeneralJournalCredit generalJournalCredit, BigDecimal debitAmount) {
        return LedgerDebit.builder()
                .date(generalJournal.getDate())
                .desc(generalJournalCredit.getDesc())
                .amount(debitAmount)
                .reference(generalJournal.getId())
                .build();
    }

    private LedgerCredit buildLedgerCredit(GeneralJournal generalJournal, GeneralJournalDebit generalJournalDebit, BigDecimal creditAmount) {
        return LedgerCredit.builder()
                .date(generalJournal.getDate())
                .desc(generalJournalDebit.getDesc())
                .amount(creditAmount)
                .reference(generalJournal.getId())
                .build();
    }

    private BigDecimal diffSumDebitCreditOf(Ledger ledger) {
        BigDecimal diff = ledger.getSumDebit().abs().subtract(ledger.getSumCredit().abs()); //.abs();
        if (ledger.getCode().equalsIgnoreCase("3004")) {
            log.info("break");
        }
        if (ledger.getSumDebit().compareTo(BigDecimal.ZERO) > 0
                || ledger.getSumCredit().compareTo(BigDecimal.ZERO) > 0) {
            diff = diff.abs();
        }
        return diff;
    }

    private BigDecimal sumOfCredit(List<LedgerCredit> credits) {
        return credits.stream().map(LedgerCredit::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumOfDebit(List<LedgerDebit> debits) {
        return debits.stream().map(LedgerDebit::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isExpenseWithVat(String code) {
        return code.equalsIgnoreCase(EXPENSE_VAT_BUY_CODE);
    }

    private boolean isWithHoldingTax(String code) {
        return code.equalsIgnoreCase(WITHHOLDING_TAX_CODE);
    }

    private boolean isCreditContainWithHoldingTax(GeneralJournal generalJournal) {
        return generalJournal.getCredits().stream()
                .anyMatch(credit -> isWithHoldingTax(credit.getCode()));
    }

}
