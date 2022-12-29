package th.co.readypaper.billary.accounting.report.ledger.model;

import lombok.Data;
import th.co.readypaper.billary.repo.entity.account.ledger.Ledger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class LedgerEntryDto {
    private String id;
    private Ledger ledger;
    private LocalDate date;
    private String desc;
    private BigDecimal amount;
    private UUID reference;
}
