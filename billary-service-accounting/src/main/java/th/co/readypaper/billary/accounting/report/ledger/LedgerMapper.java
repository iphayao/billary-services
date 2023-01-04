package th.co.readypaper.billary.accounting.report.ledger;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import th.co.readypaper.billary.accounting.report.ledger.model.LedgerDto;
import th.co.readypaper.billary.repo.entity.account.ledger.Ledger;

@Mapper(componentModel = "spring")
public interface LedgerMapper {
    LedgerDto toDto(Ledger ledger);

    @Mapping(target = "id", ignore = true)
    Ledger toEntity(LedgerDto ledgerDto);

    @Mapping(target = "id", ignore = true)
    Ledger update(LedgerDto updateLedger, @MappingTarget Ledger ledger);

}
