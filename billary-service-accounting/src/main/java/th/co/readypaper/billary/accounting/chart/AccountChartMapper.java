package th.co.readypaper.billary.accounting.chart;

import org.mapstruct.Mapper;
import th.co.readypaper.billary.common.model.dto.accounting.chart.AccountChartDto;
import th.co.readypaper.billary.repo.entity.account.chart.AccountChart;

@Mapper(componentModel = "spring")
public interface AccountChartMapper {

    AccountChartDto toDto(AccountChart accountChart);
}
