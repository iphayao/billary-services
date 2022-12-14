package th.co.readypaper.billary.accounting.tax.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithholdingTaxReport {
    private String taxYear;
    private String taxMonth;
    private String taxName;
    private String taxId;
    private String taxOffice;
    private String taxAddress;
    private String taxOrganisation;
    private List<WithholdingTax> entries;
    private BigDecimal totalPaymentAmount;
    private BigDecimal totalPaidAmount;
    private BigDecimal totalWithholdingTaxAmount;
}
