package th.co.readypaper.billary.sales.quotation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import th.co.readypaper.billary.repo.entity.quotation.Quotation;
import th.co.readypaper.billary.repo.entity.quotation.QuotationLineItem;
import th.co.readypaper.billary.common.model.LineItemMapper;
import th.co.readypaper.billary.sales.common.model.document.DocumentDto;
import th.co.readypaper.billary.sales.quotation.model.dto.QuotationDto;
import th.co.readypaper.billary.sales.quotation.model.dto.QuotationLineItemDto;

@Mapper(componentModel = "spring")
public interface QuotationMapper extends LineItemMapper<QuotationLineItem> {

    QuotationDto toDto(Quotation entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    Quotation toEntity(QuotationDto dto);

    QuotationLineItem quotationLineItemDtoToQuotationLineItem(QuotationLineItemDto dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "lineItems", qualifiedByName = "lineItems")
    })
    Quotation update(@MappingTarget Quotation target, Quotation source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    QuotationLineItem update(@MappingTarget QuotationLineItem target, QuotationLineItem source);

    DocumentDto toDocument(Quotation quotation);

}
