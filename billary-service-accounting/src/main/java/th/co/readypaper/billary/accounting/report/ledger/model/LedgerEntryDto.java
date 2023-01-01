package th.co.readypaper.billary.accounting.report.ledger.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class LedgerEntryDto {
    private String id;
    private LocalDate date;
    private String desc;
    private BigDecimal amount;
    private UUID reference;
}
