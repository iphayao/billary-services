package th.co.readypaper.billary.repo.entity.expense;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class ExpenseVatType {
    @Id
    private int id;
    private String name;
    private String description;
}
