package th.co.readypaper.billary.accounting.journal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import th.co.readypaper.billary.accounting.journal.model.dto.JournalEntryDto;
import th.co.readypaper.billary.repo.entity.journal.JournalEntry;

@Mapper(componentModel = "spring")
public interface JournalEntryMapper {

    JournalEntryDto toDto(JournalEntry entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    JournalEntry toEntity(JournalEntryDto dto);
}
