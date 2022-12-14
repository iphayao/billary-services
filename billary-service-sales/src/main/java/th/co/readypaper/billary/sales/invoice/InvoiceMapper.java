package th.co.readypaper.billary.sales.invoice;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import th.co.readypaper.billary.repo.entity.invoice.Invoice;
import th.co.readypaper.billary.repo.entity.invoice.InvoiceLineItem;
import th.co.readypaper.billary.common.model.LineItemMapper;
import th.co.readypaper.billary.sales.common.model.document.DocumentDto;
import th.co.readypaper.billary.sales.invoice.model.dto.InvoiceDto;
import th.co.readypaper.billary.sales.invoice.model.dto.InvoiceLineItemDto;

@Mapper(componentModel = "spring")
public interface InvoiceMapper extends LineItemMapper<InvoiceLineItem> {

    InvoiceDto toDto(Invoice invoice);

    InvoiceLineItemDto invoiceLineItemToInvoiceLineItemDto(InvoiceLineItem entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    Invoice toEntity(InvoiceDto dto);

    InvoiceLineItem invoiceLineItemDtoToInvoiceLineItem(InvoiceLineItemDto dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "lineItems", qualifiedByName = "lineItems")
    })
    Invoice update(@MappingTarget Invoice target, Invoice source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    InvoiceLineItem update(@MappingTarget InvoiceLineItem target, InvoiceLineItem source);

    DocumentDto toDocument(Invoice invoice);
}
