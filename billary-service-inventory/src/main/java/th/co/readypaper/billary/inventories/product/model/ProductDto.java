package th.co.readypaper.billary.inventories.product.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private String id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer typeId;
    private String typeName;
    private Integer categoryId;
    private String categoryName;
    private Integer unitId;
    private String unitName;
    private Integer vatTypeId;
    private String vatTypeName;
    private Integer inventoryTypeId;
    private String inventoryTypeName;
}
