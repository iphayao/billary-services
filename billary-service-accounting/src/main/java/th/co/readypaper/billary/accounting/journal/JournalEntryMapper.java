package th.co.readypaper.billary.accounting.journal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import th.co.readypaper.billary.accounting.journal.model.dto.JournalEntryDto;
import th.co.readypaper.billary.common.model.LineItemMapper;
import th.co.readypaper.billary.repo.entity.journal.JournalEntry;
import th.co.readypaper.billary.repo.entity.journal.JournalEntryLineItem;

@Mapper(componentModel = "spring")
public interface JournalEntryMapper extends LineItemMapper<JournalEntryLineItem> {

    JournalEntryDto toDto(JournalEntry entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    JournalEntry toEntity(JournalEntryDto dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "lineItems", qualifiedByName = "lineItems")
    })
    JournalEntry update(@MappingTarget JournalEntry target, JournalEntry source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    JournalEntryLineItem update(@MappingTarget JournalEntryLineItem target, JournalEntryLineItem source);
}
