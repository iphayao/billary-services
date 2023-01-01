package th.co.readypaper.billary.accounting.report.ledger.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LedgerDto {
    private String id;
    private String code;
    private String desc;
    private int year;
    private int month;
    private List<LedgerEntryDto> debits;
    private List<LedgerEntryDto> credits;
    private BigDecimal sumDebit;
    private BigDecimal sumCredit;
    private BigDecimal diffSumDebitCredit;
}
