package th.co.readypaper.billary.accounting.report.journal.builder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.accounting.voucher.JournalVoucherRepository;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournal;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalCredit;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalDebit;
import th.co.readypaper.billary.repo.entity.voucher.JournalVoucher;
import th.co.readypaper.billary.repo.repository.AccountChartRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GeneralJournalVoucherBuilder {
    private final AccountChartRepository accountChartRepository;
    private final JournalVoucherRepository journalVoucherRepository;


    public GeneralJournalVoucherBuilder(AccountChartRepository accountChartRepository,
                                        JournalVoucherRepository journalVoucherRepository) {
        this.accountChartRepository = accountChartRepository;
        this.journalVoucherRepository = journalVoucherRepository;
    }

    public List<GeneralJournal> build(LocalDate startDate, LocalDate endDate) {
        return journalVoucherRepository.findByIssuedDateBetween(startDate, endDate)
                .parallelStream().parallel()
                .map(this::buildGeneralJournal)
                .collect(Collectors.toList());
    }

    private GeneralJournal buildGeneralJournal(JournalVoucher journalVoucher) {
        log.info("Journal Voucher DocumentID: {}", journalVoucher.getDocumentId());
        var generalJournal = GeneralJournal.builder()
                .reference(journalVoucher.getId())
                .referenceDate(journalVoucher.getIssuedDate())
                .documentId(journalVoucher.getDocumentId())
                .description(descOf(journalVoucher))
                .type("Journal Voucher")
                .date(journalVoucher.getIssuedDate())
                .debits(buildGeneralJournalDebit(journalVoucher))
                .credits(buildGeneralJournalCredit(journalVoucher))
                .build();

        generalJournal.setDebits(generalJournal.getDebits().stream()
                .peek(debit -> debit.setGeneralJournal(generalJournal))
                .toList());
        generalJournal.setCredits(generalJournal.getCredits().stream()
                .peek(credit -> credit.setGeneralJournal(generalJournal))
                .toList());

        return generalJournal;
    }

    private List<GeneralJournalDebit> buildGeneralJournalDebit(JournalVoucher journalVoucher) {
        var debits = new ArrayList<GeneralJournalDebit>();
        journalVoucher.getLineItems().forEach(lineItem -> {
            if (lineItem.getDebitAmount().compareTo(BigDecimal.ZERO) > 0) {
                debits.add(GeneralJournalDebit.builder()
                        .code(lineItem.getAccountChart().getCode())
                        .desc(lineItem.getAccountChart().getName())
                        .amount(lineItem.getDebitAmount())
                        .build());
            }
        });
        return debits;
    }

    private List<GeneralJournalCredit> buildGeneralJournalCredit(JournalVoucher journalVoucher) {
        var credits = new ArrayList<GeneralJournalCredit>();
        journalVoucher.getLineItems().forEach(lineItem -> {
            if (lineItem.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
                credits.add(GeneralJournalCredit.builder()
                        .code(lineItem.getAccountChart().getCode())
                        .desc(lineItem.getAccountChart().getName())
                        .amount(lineItem.getCreditAmount())
                        .build());
            }
        });
        return credits;
    }

    private String descOf(JournalVoucher journalVoucher) {
        String desc = journalVoucher.getDescription();

        if (desc.startsWith("ชำระ")) {
            return desc + " ให้กับ " + journalVoucher.getContact().getName();
        } else if (desc.startsWith("รับ") || desc.startsWith("กู้ยืม") || desc.startsWith("โอน")) {
            return desc + " จาก " + journalVoucher.getContact().getName();
        } else {
            return desc + " " + journalVoucher.getContact().getName();
        }
    }
}
