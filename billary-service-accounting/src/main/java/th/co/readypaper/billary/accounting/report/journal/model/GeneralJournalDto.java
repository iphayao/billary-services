package th.co.readypaper.billary.accounting.report.journal.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GeneralJournalDto {
    private String id;
    private LocalDate date;
    private String desc;
    private String type;
    private String ref;
    private String documentId;
    private List<GeneralJournalEntry> debit;
    private List<GeneralJournalEntry> credit;
}
