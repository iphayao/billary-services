package th.co.readypaper.billary.common.model.dto.expense;

import lombok.Data;

@Data
public class ExpenseStatusDto {
    private Integer id;
    private String name;
    private String code;
    private String description;
}
