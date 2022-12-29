package th.co.readypaper.billary.accounting.report.ledger;

import org.mapstruct.Mapper;
import th.co.readypaper.billary.accounting.report.ledger.model.LedgerDto;
import th.co.readypaper.billary.repo.entity.account.ledger.Ledger;

@Mapper(componentModel = "spring")
public interface LedgerMapper {
    LedgerDto toDto(Ledger ledger);
}
