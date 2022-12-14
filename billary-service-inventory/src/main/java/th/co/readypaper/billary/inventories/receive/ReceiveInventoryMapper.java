package th.co.readypaper.billary.inventories.receive;

import org.mapstruct.*;
import th.co.readypaper.billary.repo.entity.receive.ReceiveInventory;
import th.co.readypaper.billary.repo.entity.receive.ReceiveInventoryLineItem;
import th.co.readypaper.billary.inventories.receive.model.dto.ReceiveInventoryDto;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReceiveInventoryMapper {
    ReceiveInventoryDto toDto(ReceiveInventory entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
//            @Mapping(target = "version", ignore = true)
    })
    ReceiveInventory toEntity(ReceiveInventoryDto dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
//            @Mapping(target = "version", ignore = true),
            @Mapping(target = "contact.id", ignore = true),
            @Mapping(target = "contact.createdAt", ignore = true),
            @Mapping(target = "contact.createdBy", ignore = true),
            @Mapping(target = "contact.updatedAt", ignore = true),
            @Mapping(target = "contact.updatedBy", ignore = true),
//            @Mapping(target = "contact.version", ignore = true),
            @Mapping(target = "lineItems", qualifiedByName = "lineItems")
    })
    ReceiveInventory update(@MappingTarget ReceiveInventory target, ReceiveInventory source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "receiveInventory", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
//            @Mapping(target = "version", ignore = true)
    })
    ReceiveInventoryLineItem update(@MappingTarget ReceiveInventoryLineItem target, ReceiveInventoryLineItem source);

    @Named("lineItems")
    default void update(@MappingTarget List<ReceiveInventoryLineItem> target, List<ReceiveInventoryLineItem> source) {
        List<ReceiveInventoryLineItem> items = new ArrayList<>();
        if (target.size() == source.size()) {
            for (int i = 0; i < target.size(); i++) {
                if (target.get(i).getId().equals(source.get(i).getId())) {
                    items.add(update(target.get(i), source.get(i)));
                }
            }
        } else if (target.size() < source.size()) {
            for (int i = 0; i < source.size(); i++) {
                if (i < target.size()) {
                    if (target.get(i).getId().equals(source.get(i).getId())) {
                        items.add(update(target.get(i), source.get(i)));
                    }
                } else {
                    items.add(source.get(i));
                }
            }
        } else {
            for (int i = 0; i < target.size(); i++) {
                if (i < source.size()) {
                    if (target.get(i).getId().equals(source.get(i).getId())) {
                        items.add(update(target.get(i), source.get(i)));
                    }
                }
            }
        }

        target.clear();
        target.addAll(items);
    }

}
