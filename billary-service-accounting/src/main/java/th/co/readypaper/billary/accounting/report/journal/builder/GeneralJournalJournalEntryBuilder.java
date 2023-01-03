package th.co.readypaper.billary.accounting.report.journal.builder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.accounting.journal.JournalEntryRepository;
import th.co.readypaper.billary.accounting.journal.model.dto.JournalEntryLineItemDto;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournal;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalCredit;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalDebit;
import th.co.readypaper.billary.repo.entity.journal.JournalEntry;
import th.co.readypaper.billary.repo.entity.journal.JournalEntryLineItem;
import th.co.readypaper.billary.repo.repository.AccountChartRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GeneralJournalJournalEntryBuilder {
    private final AccountChartRepository accountChartRepository;
    private final JournalEntryRepository journalEntryRepository;

    public GeneralJournalJournalEntryBuilder(AccountChartRepository accountChartRepository,
                                             JournalEntryRepository journalEntryRepository) {
        this.accountChartRepository = accountChartRepository;
        this.journalEntryRepository = journalEntryRepository;
    }

    public List<GeneralJournal> build(LocalDate startDate, LocalDate endDate) {
        return journalEntryRepository.findByIssuedDateBetween(startDate, endDate)
                .parallelStream().parallel()
                .map(this::buildGeneralJournal)
                .collect(Collectors.toList());
    }

    private GeneralJournal buildGeneralJournal(JournalEntry journalEntry) {
        log.info("Journal Entry DocumentID: {}", journalEntry.getDocumentId());
        var generalJournal = GeneralJournal.builder()
                .reference(journalEntry.getId())
                .documentId(journalEntry.getDocumentId())
                .description(descOf(journalEntry))
                .type("Journal Entry")
                .date(journalEntry.getIssuedDate())
                .debits(buildGeneralJournalDebit(journalEntry))
                .credits(buildGeneralJournalCredit(journalEntry))
                .build();

        generalJournal.setDebits(generalJournal.getDebits().stream()
                .peek(debit -> debit.setGeneralJournal(generalJournal))
                .toList());
        generalJournal.setCredits(generalJournal.getCredits().stream()
                .peek(credit -> credit.setGeneralJournal(generalJournal))
                .toList());

        return generalJournal;
    }

    private List<GeneralJournalDebit> buildGeneralJournalDebit(JournalEntry journalEntry) {
        var debits = new ArrayList<GeneralJournalDebit>();
        journalEntry.getLineItems().forEach(journalEntryLineItem -> {
            debits.add(GeneralJournalDebit.builder()
                    .code(journalEntryLineItem.getDebitAccountChart().getCode())
                    .desc(journalEntryLineItem.getDebitAccountChart().getName())
                    .amount(journalEntryLineItem.getLineAmount())
                    .build());
        });

        return debits;
    }

    private List<GeneralJournalCredit> buildGeneralJournalCredit(JournalEntry journalEntry) {
        var credits = new ArrayList<GeneralJournalCredit>();
        journalEntry.getLineItems().forEach(journalEntryLineItem -> {
            credits.add(GeneralJournalCredit.builder()
                    .code(journalEntryLineItem.getCreditAccountChart().getCode())
                    .desc(journalEntryLineItem.getCreditAccountChart().getName())
                    .amount(journalEntryLineItem.getLineAmount())
                    .build());
        });
        return credits;
    }

    private String descOf(JournalEntry journalEntry) {
        String desc = journalEntry.getLineItems().stream()
                .map(JournalEntryLineItem::getDescription)
                .collect(Collectors.joining(" "));

        if (desc.startsWith("ชำระ")) {
            return desc + " ให้กับ " + journalEntry.getContact().getName();
        } else if (desc.startsWith("รับ") || desc.startsWith("กู้ยืม") || desc.startsWith("โอน")) {
            return desc + " จาก " + journalEntry.getContact().getName();
        } else {
            return desc + " " + journalEntry.getContact().getName();
        }
    }

}
