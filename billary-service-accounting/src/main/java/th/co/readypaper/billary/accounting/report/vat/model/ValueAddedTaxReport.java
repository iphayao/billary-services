package th.co.readypaper.billary.accounting.report.vat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValueAddedTaxReport {
    private VatReportType type;
    private String taxYear;
    private String taxMonth;
    private String taxName;
    private String taxId;
    private String taxOffice;
    private String taxAddress;
    private String taxOrganisation;
    private List<ValueAddedTax> entries;
    private BigDecimal totalVatableAmount;
    private BigDecimal totalVatAmount;
    private BigDecimal totalAmount;
}
