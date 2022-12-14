package th.co.readypaper.billary.sales.billing;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import th.co.readypaper.billary.repo.entity.billing.BillingNote;
import th.co.readypaper.billary.repo.entity.billing.BillingNoteLineItem;
import th.co.readypaper.billary.sales.billing.model.dto.BillingNoteDto;
import th.co.readypaper.billary.sales.billing.model.dto.BillingNoteLineItemDto;
import th.co.readypaper.billary.common.model.LineItemMapper;
import th.co.readypaper.billary.sales.common.model.document.DocumentDto;

@Mapper(componentModel = "spring")
public interface BillingNoteMapper extends LineItemMapper<BillingNoteLineItem> {
    BillingNoteDto toDto(BillingNote entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    BillingNote toEntity(BillingNoteDto dto);

    BillingNoteLineItem billingNoteLineItemDtoToBillingNoteLineItem(BillingNoteLineItemDto dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "lineItems", qualifiedByName = "lineItems")
    })
    BillingNote update(@MappingTarget BillingNote target, BillingNote source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    BillingNoteLineItem update(@MappingTarget BillingNoteLineItem target, BillingNoteLineItem source);

    DocumentDto toDocument(BillingNote billingNote);
}
