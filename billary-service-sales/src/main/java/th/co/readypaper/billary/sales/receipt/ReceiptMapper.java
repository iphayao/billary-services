package th.co.readypaper.billary.sales.receipt;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import th.co.readypaper.billary.repo.entity.receipt.Receipt;
import th.co.readypaper.billary.repo.entity.receipt.ReceiptLineItem;
import th.co.readypaper.billary.repo.entity.receipt.ReceiptPayment;
import th.co.readypaper.billary.repo.entity.receipt.ReceiptPaymentType;
import th.co.readypaper.billary.common.model.LineItemMapper;
import th.co.readypaper.billary.sales.common.model.document.DocumentDto;
import th.co.readypaper.billary.sales.receipt.model.dto.ReceiptDto;
import th.co.readypaper.billary.sales.receipt.model.dto.ReceiptPaymentTypeDto;

@Mapper(componentModel = "spring")
public interface ReceiptMapper extends LineItemMapper<ReceiptLineItem> {

    ReceiptDto toDto(Receipt entity);
    ReceiptPaymentTypeDto toDto(ReceiptPaymentType entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    Receipt toEntity(ReceiptDto dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "lineItems", qualifiedByName = "lineItems")
    })
    Receipt update(@MappingTarget Receipt target, Receipt source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    ReceiptLineItem update(@MappingTarget ReceiptLineItem target, ReceiptLineItem source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "receipt", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    ReceiptPayment update(@MappingTarget ReceiptPayment target, ReceiptPayment source);

    DocumentDto toDocument(Receipt receipt);

}
