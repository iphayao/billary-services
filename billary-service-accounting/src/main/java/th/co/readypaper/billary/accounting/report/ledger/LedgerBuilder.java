package th.co.readypaper.billary.accounting.report.ledger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.accounting.report.journal.GeneralJournalRepository;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournal;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalCredit;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalDebit;
import th.co.readypaper.billary.repo.entity.account.ledger.Ledger;
import th.co.readypaper.billary.repo.entity.account.ledger.LedgerCredit;
import th.co.readypaper.billary.repo.entity.account.ledger.LedgerDebit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static th.co.readypaper.billary.accounting.common.Constants.EXPENSE_VAT_BUY_CODE;
import static th.co.readypaper.billary.accounting.common.Constants.WITHHOLDING_TAX_CODE;
import static th.co.readypaper.billary.common.utils.DateUtils.firstDayOf;
import static th.co.readypaper.billary.common.utils.DateUtils.lastDayOf;

@Slf4j
@Component
public class LedgerBuilder {
    public static final String BROUGHT_FORWARD_DESC = "ยอดยกมา";
    private final LedgerRepository ledgerRepository;
    private final GeneralJournalRepository generalJournalRepository;

    public LedgerBuilder(LedgerRepository ledgerRepository,
                         GeneralJournalRepository generalJournalRepository) {
        this.ledgerRepository = ledgerRepository;
        this.generalJournalRepository = generalJournalRepository;
    }

    public List<Ledger> buildByYearAndMonth(Integer year, Integer month) {
        log.info("Year: {}", year);
        log.info("Month: {}", month);

        LocalDate firstDayOfMonth = firstDayOf(year, month);
        LocalDate lastDayOfMonth = lastDayOf(year, month);

        log.info("First day of month: {}", firstDayOfMonth);
        log.info("Last day of month: {}", lastDayOfMonth);

        LocalDate previousMonth = firstDayOfMonth.minusMonths(1);

        int broughtForwardYear;
        int broughtForwardMonth;

        if (month == 1) {
            broughtForwardYear = year;
            broughtForwardMonth = 0;
        } else {
            broughtForwardYear = previousMonth.getYear();
            broughtForwardMonth = previousMonth.getMonthValue();
        }

        ledgerRepository.deleteByYearAndMonth(year, month)
                .ifPresent(count -> log.info("Count: {}", count));

        final Map<String, Ledger> ledgers;

        List<Ledger> broughtForwardLedgers = ledgerRepository.findByYearAndMonth(broughtForwardYear, broughtForwardMonth);
        ledgers = buildBroughtForward(broughtForwardLedgers, firstDayOfMonth); // ยอดยกมา

        generalJournalRepository.findByDateBetween(firstDayOfMonth, lastDayOfMonth, Sort.by(Sort.Direction.ASC, "date"))
                .forEach(generalJournal -> createLedgers(ledgers, generalJournal));

        return ledgers.values().stream()
                .map(ledgerRepository::save)
                .toList();
    }

    private Map<String, Ledger> buildBroughtForward(List<Ledger> previousLedgers, LocalDate firstDayOfMonth) {
        var ledgers = new TreeMap<String, Ledger>();
        previousLedgers.forEach(ledger -> {
            var broughtForwardLedger = Ledger.builder()
                    .code(ledger.getCode())
                    .desc(ledger.getDesc())
                    .year(firstDayOfMonth.getYear())
                    .month(firstDayOfMonth.getMonthValue())
                    .debits(buildBroughtForwardDebit(firstDayOfMonth, ledger))
                    .credits(buildBroughtForwardCredit(firstDayOfMonth, ledger))
                    .diffSumDebitCredit(diffSumDebitCreditOf(ledger))
                    .build();

            broughtForwardLedger.setSumDebit(sumOfDebit(broughtForwardLedger.getDebits()));
            broughtForwardLedger.setSumCredit(sumOfCredit(broughtForwardLedger.getCredits()));

            broughtForwardLedger.setDebits(broughtForwardLedger.getDebits().stream()
                    .peek(ledgerDebit -> ledgerDebit.setLedger(broughtForwardLedger))
                    .collect(Collectors.toList()));
            broughtForwardLedger.setCredits(broughtForwardLedger.getCredits().stream()
                    .peek(ledgerCredit -> ledgerCredit.setLedger(broughtForwardLedger))
                    .collect(Collectors.toList()));

            ledgers.put(ledger.getCode(), broughtForwardLedger);
        });
        return ledgers;
    }

    public void createLedgers(Map<String, Ledger> ledgers, GeneralJournal generalJournal) {
        log.info("{}", generalJournal.getDocumentId());
        // สร้างบัญชีแยกประเภท เดบิต
        generalJournal.getDebits()
                .forEach(generalJournalDebit -> {
                    String accountCode = generalJournalDebit.getCode();

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
                    if (ledgers.containsKey(accountCode)) {
                        // เพิ่มไปในบัญชีแยกประเภทที่มีอยู่
                        ledger = ledgers.get(accountCode);
                        ledger.getDebits().addAll(debits);
                    } else {
                        // สร้างบัญชีแยกประเภทใหม่
                        ledger = new Ledger();
                        ledger.setYear(generalJournal.getDate().getYear());
                        ledger.setMonth(generalJournal.getDate().getMonthValue());
                        ledger.setCode(accountCode);
                        ledger.setDesc(generalJournalDebit.getDesc());
                        ledger.setDebits(debits);
                        ledger.setCredits(new ArrayList<>());
                    }

                    ledger.setSumDebit(sumOfDebit(ledger.getDebits()));
                    ledger.setSumCredit(sumOfCredit(ledger.getCredits()));
                    ledger.setDiffSumDebitCredit(diffSumDebitCreditOf(ledger));

                    ledger.setDebits(ledger.getDebits().stream()
                            .peek(ledgerDebit -> ledgerDebit.setLedger(ledger))
                            .collect(Collectors.toList()));
                    ledger.setCredits(ledger.getCredits().stream()
                            .peek(ledgerCredit -> ledgerCredit.setLedger(ledger))
                            .collect(Collectors.toList()));

                    ledgers.put(accountCode, ledger);
                });

        // สร้างบัญชีแยกประเภท เครดิต
        generalJournal.getCredits()
                .forEach(generalJournalCredit -> {
                    String accountCode = generalJournalCredit.getCode();

                    List<LedgerCredit> credits = new ArrayList<>();

                    if (isWithHoldingTax(accountCode)) {
                        generalJournal.getDebits()
                                .forEach(generalJournalDebit -> {
                                    if (!isExpenseWithVat(generalJournalDebit.getCode())) {
                                        credits.add(buildLedgerCredit(generalJournal, generalJournalDebit, generalJournalCredit.getAmount()));
                                    }
                                });
                    } else {
                        if (generalJournal.getDebits().size() < 2) {
                            generalJournal.getDebits()
                                    .forEach(generalJournalDebit -> {
                                        if (isCreditContainWithHoldingTax(generalJournal)
                                                && !isExpenseWithVat(generalJournalDebit.getCode())) {
                                            // รายการค่าใช้จ่ายที่ต้องหัก ภาษีหัก ณ ที่จ่ายออก
                                            credits.add(buildLedgerCredit(generalJournal, generalJournalDebit, generalJournalCredit.getAmount()));
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
                    }

                    Ledger ledger;
                    if (ledgers.containsKey(accountCode)) {
                        // เพิ่มไปในบัญชีแยกประเภทที่มีอยู่
                        ledger = ledgers.get(accountCode);
                        ledger.getCredits().addAll(credits);
                    } else {
                        // สร้างบัญชีแยกประเภทใหม่
                        ledger = new Ledger();
                        ledger.setYear(generalJournal.getDate().getYear());
                        ledger.setMonth(generalJournal.getDate().getMonthValue());
                        ledger.setCode(accountCode);
                        ledger.setDesc(generalJournalCredit.getDesc());
                        ledger.setDebits(new ArrayList<>());
                        ledger.setCredits(credits);
                    }

                    ledger.setSumDebit(sumOfDebit(ledger.getDebits()));
                    ledger.setSumCredit(sumOfCredit(ledger.getCredits()));
                    ledger.setDiffSumDebitCredit(diffSumDebitCreditOf(ledger));

                    ledger.setDebits(ledger.getDebits().stream()
                            .peek(ledgerDebit -> ledgerDebit.setLedger(ledger))
                            .collect(Collectors.toList()));
                    ledger.setCredits(ledger.getCredits().stream()
                            .peek(ledgerCredit -> ledgerCredit.setLedger(ledger))
                            .collect(Collectors.toList()));

                    ledgers.put(accountCode, ledger);
                });
    }

    private List<LedgerDebit> buildBroughtForwardDebit(LocalDate date, Ledger ledger) {
        var debits = new ArrayList<LedgerDebit>();

        if (ledger != null && ledger.getSumDebit().abs().compareTo(ledger.getSumCredit().abs()) > 0) {
            debits.add(LedgerDebit.builder()
                    .date(date)
                    .desc(BROUGHT_FORWARD_DESC)
                    .amount(ledger.getDiffSumDebitCredit())
                    .reference(ledger.getId())
                    .build());
        }
        return debits;
    }

    private List<LedgerCredit> buildBroughtForwardCredit(LocalDate date, Ledger ledger) {
        var credits = new ArrayList<LedgerCredit>();

        if (ledger != null && ledger.getSumCredit().abs().compareTo(ledger.getSumDebit().abs()) > 0) {
            credits.add(LedgerCredit.builder()
                    .date(date)
                    .desc(BROUGHT_FORWARD_DESC)
                    .amount(ledger.getDiffSumDebitCredit())
                    .reference(ledger.getId())
                    .build());
        }
        return credits;
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
        if (ledger.getSumDebit().compareTo(BigDecimal.ZERO) > 0
                || ledger.getSumCredit().compareTo(BigDecimal.ZERO) > 0) {
            diff = diff.abs();
        }
        return diff.setScale(2, RoundingMode.HALF_DOWN);
    }

    private BigDecimal sumOfDebit(List<LedgerDebit> debits) {
        return debits.stream().map(LedgerDebit::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(4, RoundingMode.HALF_DOWN);
    }

    private BigDecimal sumOfCredit(List<LedgerCredit> credits) {
        return credits.stream().map(LedgerCredit::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(4, RoundingMode.HALF_DOWN);
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
