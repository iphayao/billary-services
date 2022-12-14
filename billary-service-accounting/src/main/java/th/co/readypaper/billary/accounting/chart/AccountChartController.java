package th.co.readypaper.billary.accounting.chart;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.readypaper.billary.common.model.dto.accounting.chart.AccountChartDto;
import th.co.readypaper.billary.common.model.ApiResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account-charts")
public class AccountChartController {
    private final AccountChartService accountchartService;

    public AccountChartController(AccountChartService accountchartService) {
        this.accountchartService = accountchartService;
    }

    @GetMapping
    public Optional<ApiResponse<List<AccountChartDto>>> getAccountCharts() {
        List<AccountChartDto> charts = accountchartService.findAccountCharts();
        return Optional.of(charts)
                .map(ApiResponse::success);
    }

    @GetMapping("/{code}")
    public Optional<ApiResponse<AccountChartDto>> getAccountChart(@PathVariable String code) {
        return accountchartService.findAccountChartByCode(code)
                .map(ApiResponse::success);
    }

}
