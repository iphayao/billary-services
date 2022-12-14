package th.co.readypaper.billary.sales.receipt.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReceiptPaymentDto {
    private String id;
    private BigDecimal paidAmount;
    private LocalDate paymentDate;
    private ReceiptPaymentTypeDto paymentType;
    private String remark;
}
