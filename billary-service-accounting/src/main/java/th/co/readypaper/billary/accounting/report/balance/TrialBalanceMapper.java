package th.co.readypaper.billary.accounting.report.balance;

import org.mapstruct.Mapper;
import th.co.readypaper.billary.accounting.report.balance.model.TrialBalanceDto;
import th.co.readypaper.billary.repo.entity.account.balance.TrialBalance;

@Mapper(componentModel = "spring")
public interface TrialBalanceMapper {

    TrialBalanceDto toDto(TrialBalance entity);
}
