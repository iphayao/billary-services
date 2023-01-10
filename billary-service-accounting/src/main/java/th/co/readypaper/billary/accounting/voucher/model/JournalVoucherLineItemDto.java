package th.co.readypaper.billary.accounting.voucher.model;

import lombok.Data;
import th.co.readypaper.billary.common.model.dto.accounting.chart.AccountChartDto;

import java.math.BigDecimal;

@Data
public class JournalVoucherLineItemDto {
    private String id;
    private int itemOrder;
    private AccountChartDto accountChart;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
}
