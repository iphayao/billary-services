package th.co.readypaper.billary.common.model.dto.expense;

import lombok.Data;
import th.co.readypaper.billary.common.model.dto.contact.ContactDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ExpenseDto {
    private String id;
    private String documentId;
    private String reference;
    private ContactDto contact;
    private LocalDate issuedDate;
    private int lineVatTypeId;
    private ExpenseStatusDto status;
    private List<ExpenseLineItemDto> lineItems;
    private boolean vatInclusive;
    private boolean useInputTax;
    private BigDecimal subTotal;
    private BigDecimal totalDiscount;
    private BigDecimal totalAfterDiscount;
    private BigDecimal exemptVatAmount;
    private BigDecimal vatableAmount;
    private BigDecimal vatAmount;
    private BigDecimal total;
    private WithholdingTaxPercentDto withholdingTaxPercent;
    private BigDecimal withholdingTaxAmount;
    private BigDecimal paymentAmount;
    private String remark;
    private ExpensePaymentDto payment;
}
