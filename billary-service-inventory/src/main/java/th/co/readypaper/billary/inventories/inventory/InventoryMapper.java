package th.co.readypaper.billary.inventories.inventory;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import th.co.readypaper.billary.repo.entity.inventory.Inventory;
import th.co.readypaper.billary.inventories.inventory.model.dto.InventoryDto;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "product.typeId", source = "product.type.id")
    @Mapping(target = "product.typeName", source = "product.type.name")
    @Mapping(target = "product.categoryId", source = "product.category.id")
    @Mapping(target = "product.categoryName", source = "product.category.name")
    @Mapping(target = "product.unitId", source = "product.unit.id")
    @Mapping(target = "product.unitName", source = "product.unit.name")
    @Mapping(target = "product.vatTypeId", source = "product.vatType.id")
    @Mapping(target = "product.vatTypeName", source = "product.vatType.name")
    @Mapping(target = "product.inventoryTypeId", source = "product.inventoryType.id")
    @Mapping(target = "product.inventoryTypeName", source = "product.inventoryType.name")
    InventoryDto toDto(Inventory entity);

}
