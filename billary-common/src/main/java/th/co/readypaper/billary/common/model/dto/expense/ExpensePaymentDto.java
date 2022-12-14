package th.co.readypaper.billary.common.model.dto.expense;

import lombok.Data;
import th.co.readypaper.billary.repo.entity.expense.ExpensePaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpensePaymentDto {
    private String id;
    private BigDecimal paidAmount;
    private LocalDate paymentDate;
    private ExpensePaymentTypeDto paymentType;
    private String remark;
}
