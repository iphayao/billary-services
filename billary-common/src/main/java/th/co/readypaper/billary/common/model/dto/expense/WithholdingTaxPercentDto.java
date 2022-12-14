package th.co.readypaper.billary.common.model.dto.expense;

import lombok.Data;

@Data
public class WithholdingTaxPercentDto {
    private Integer id;
    private String name;
    private int percent;
    private String description;
}
