package th.co.readypaper.billary.accounting.vat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValueAddedTax {
    private String documentId;
    private String contactName;
    private String contactTaxId;
    private String contactOffice;
    private LocalDate issuedDate;
    private BigDecimal vatableAmount;
    private BigDecimal vatAmount;
    private BigDecimal total;
}
