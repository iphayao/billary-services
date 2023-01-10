package th.co.readypaper.billary.accounting.voucher;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import th.co.readypaper.billary.accounting.voucher.model.JournalVoucherDto;
import th.co.readypaper.billary.common.model.LineItemMapper;
import th.co.readypaper.billary.repo.entity.voucher.JournalVoucher;
import th.co.readypaper.billary.repo.entity.voucher.JournalVoucherLineItem;

@Mapper(componentModel = "spring")
public interface JournalVoucherMapper extends LineItemMapper<JournalVoucherLineItem> {
    JournalVoucherDto toDto(JournalVoucher entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    JournalVoucher toEntity(JournalVoucherDto dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "lineItems", qualifiedByName = "lineItems")
    })
    JournalVoucher update(@MappingTarget JournalVoucher target, JournalVoucher source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    JournalVoucherLineItem update(@MappingTarget JournalVoucherLineItem target, JournalVoucherLineItem source);
}
