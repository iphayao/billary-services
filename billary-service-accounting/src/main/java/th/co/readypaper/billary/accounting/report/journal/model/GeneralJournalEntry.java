package th.co.readypaper.billary.accounting.report.journal.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GeneralJournalEntry {
    private String code;
    private String desc;
    private BigDecimal amount;
}
