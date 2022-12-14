package th.co.readypaper.billary.sales.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import th.co.readypaper.billary.repo.entity.inventory.Inventory;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReport {
    private String taxName;
    private String taxId;
    private String taxOffice;
    private String taxAddress;
    private String taxOrganisation;
    private Inventory inventory;
}
