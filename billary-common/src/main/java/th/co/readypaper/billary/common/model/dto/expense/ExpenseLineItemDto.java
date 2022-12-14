package th.co.readypaper.billary.common.model.dto.expense;

import lombok.Data;
import th.co.readypaper.billary.common.model.dto.accounting.chart.AccountChartDto;

import java.math.BigDecimal;

@Data
public class ExpenseLineItemDto {
    private String id;
    private String description;
    private AccountChartDto accountChart;
    private int itemOrder;
    private int quantity;
    private int vatTypeId;
    private String unitName;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal exemptVatAmount;
    private BigDecimal vatableAmount;
    private BigDecimal vatAmount;
    private BigDecimal lineAmount;
}
