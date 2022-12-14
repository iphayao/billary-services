package th.co.readypaper.billary.inventories.product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import th.co.readypaper.billary.inventories.product.model.ProductDto;
import th.co.readypaper.billary.repo.entity.product.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type.id", source = "dto.typeId")
    @Mapping(target = "category.id", source = "dto.categoryId")
    @Mapping(target = "unit.id", source = "dto.unitId")
    @Mapping(target = "vatType.id", source = "dto.vatTypeId")
    @Mapping(target = "inventoryType.id", source = "dto.inventoryTypeId")
    Product toEntity(ProductDto dto);
    @Mapping(target = "typeId", source = "type.id")
    @Mapping(target = "typeName", source = "type.name")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "unitId", source = "unit.id")
    @Mapping(target = "unitName", source = "unit.name")
    @Mapping(target = "vatTypeId", source = "vatType.id")
    @Mapping(target = "vatTypeName", source = "vatType.name")
    @Mapping(target = "inventoryTypeId", source = "inventoryType.id")
    @Mapping(target = "inventoryTypeName", source = "inventoryType.name")
    ProductDto toDto(Product entity);
    @Mapping(target = "id", ignore = true)
    Product update(@MappingTarget Product target, ProductDto source);

}
