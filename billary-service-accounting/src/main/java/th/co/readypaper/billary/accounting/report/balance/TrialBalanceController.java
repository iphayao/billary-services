package th.co.readypaper.billary.accounting.report.balance;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.accounting.common.model.AccountingYearlySummary;
import th.co.readypaper.billary.accounting.report.balance.model.TrialBalanceDto;
import th.co.readypaper.billary.common.model.ApiResponse;

import java.util.List;
import java.util.Optional;

import static th.co.readypaper.billary.common.utils.HttpHeaderUtils.trialBalanceAttachmentFilename;

@RestController
@RequestMapping("/trial-balances")
public class TrialBalanceController {
    private final TrialBalanceService trialBalanceService;

    public TrialBalanceController(TrialBalanceService trialBalanceService) {
        this.trialBalanceService = trialBalanceService;
    }

    @GetMapping
    public Optional<ApiResponse<List<TrialBalanceDto>>> getTrialBalances(@RequestParam(required = false) Integer year,
                                                                         @RequestParam(required = false) Integer month) {
        var trialBalances = trialBalanceService.findAllTrialBalances(year, month);
        return Optional.of(trialBalances)
                .map(ApiResponse::success);
    }

    @GetMapping("/summary")
    public Optional<ApiResponse<List<AccountingYearlySummary>>> getTrialBalanceYearlySummary(Integer year) {
        var yearlySummary = trialBalanceService.findTrialBalanceYearlySummary(year);
        return Optional.of(yearlySummary)
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<List<TrialBalanceDto>>> postTrialBalanceByYearMonth(@RequestParam Integer year,
                                                                                    @RequestParam(required = false) Integer month) {
        var trialBalance = trialBalanceService.createTrialBalance(year, month);
        return Optional.of(trialBalance)
                .map(ApiResponse::success);
    }

    @PostMapping("/excel")
    public ResponseEntity<byte[]> postTrialBalanceExport(@RequestParam Integer year, @RequestParam Integer month) {
        return trialBalanceService.exportTrialBalance(year, month)
                .map(data -> ResponseEntity.ok()
                        .headers(trialBalanceAttachmentFilename(year, month))
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(data))
                .orElseThrow();

    }

}
