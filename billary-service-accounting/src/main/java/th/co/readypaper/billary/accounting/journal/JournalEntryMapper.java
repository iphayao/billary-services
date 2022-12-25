package th.co.readypaper.billary.accounting.journal;

import org.mapstruct.Mapper;
import th.co.readypaper.billary.accounting.journal.model.dto.JournalEntryDto;
import th.co.readypaper.billary.repo.entity.journal.JournalEntry;

@Mapper(componentModel = "spring")
public interface JournalEntryMapper {

    JournalEntryDto toDto(JournalEntry entity);

}
