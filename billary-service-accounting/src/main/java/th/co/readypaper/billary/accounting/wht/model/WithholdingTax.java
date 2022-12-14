package th.co.readypaper.billary.accounting.wht.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class WithholdingTax {
    private TaxType taxType;
    private String documentId;
    private String contactName;
    private String contactTaxId;
    private String contactOffice;
    private LocalDate paymentDate;
    private BigDecimal paymentAmount;
    private BigDecimal paidAmount;
    private Integer withholdingTaxPercent;
    private BigDecimal withholdingTaxAmount;
    private BigDecimal total;
}
