package th.co.readypaper.billary.accounting.report.journal;

import org.mapstruct.Mapper;
import th.co.readypaper.billary.accounting.report.journal.model.GeneralJournalDto;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournal;

@Mapper(componentModel = "spring")
public interface GeneralJournalMapper {
    GeneralJournalDto toDto(GeneralJournal generalJournal);
}
