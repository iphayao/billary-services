package th.co.readypaper.billary.common.model.dto.accounting.chart;

import lombok.Data;

@Data
public class AccountChartDto {
    private String id;
    private String code;
    private String name;
    private AccountChartGroupDto group;
    private AccountChartCategoryDto category;
}
