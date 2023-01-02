package th.co.readypaper.billary.accounting.journal.model.dto;

import lombok.Data;
import th.co.readypaper.billary.common.model.dto.accounting.chart.AccountChartDto;

import java.math.BigDecimal;

@Data
public class JournalEntryLineItemDto {
    private String id;
    private AccountChartDto debitAccountChart;
    private AccountChartDto creditAccountChart;
    private int itemOrder;
    private String description;
    private int quantity;
    private String unitName;
    private int vatTypeId;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal exemptVatAmount;
    private BigDecimal vatableAmount;
    private BigDecimal vatAmount;
    private BigDecimal lineAmount;
}
