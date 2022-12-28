package th.co.readypaper.billary.accounting.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountingYearlySummary {
    private Integer index;
    private Integer year;
    private String month;
    private Integer entryCount;
}
