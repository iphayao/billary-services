package th.co.readypaper.billary.accounting.report.ledger;

import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.accounting.common.model.AccountingYearlySummary;
import th.co.readypaper.billary.accounting.report.ledger.model.LedgerDto;
import th.co.readypaper.billary.common.model.ApiResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static th.co.readypaper.billary.common.utils.HttpHeaderUtils.ledgerAttachmentFilename;

@RestController
@RequestMapping("/ledgers")
public class LedgerController {
    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @GetMapping
    public Optional<ApiResponse<List<LedgerDto>>> findAllLedgers(@RequestParam(required = false) Integer year,
                                                                 @RequestParam(required = false) Integer month) {
        var ledgers = ledgerService.findAllLedgers(year, month);
        return Optional.of(ledgers)
                .map(ApiResponse::success);
    }

    @GetMapping("/{id}")
    public Optional<ApiResponse<LedgerDto>> getLedger(@PathVariable UUID id) {
        return ledgerService.findLedgerById(id)
                .map(ApiResponse::success);
    }

    @GetMapping("/summary")
    public Optional<ApiResponse<List<AccountingYearlySummary>>> getLegerSummary(@RequestParam Integer year) {
        var legerYearlySummary = ledgerService.findLedgerYearlySummary(year);
        return Optional.of(legerYearlySummary)
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<LedgerDto>> postLedger(@RequestBody LedgerDto ledgerDto) {
        return ledgerService.createLedger(ledgerDto)
                .map(ApiResponse::success);
    }

    @PostMapping(params = {"year", "month"})
    public Optional<ApiResponse<List<LedgerDto>>> postLedger(@RequestParam Integer year,
                                                             @RequestParam(required = false) Integer month) {
        var ledgers = ledgerService.createLedger(year, month);
        return Optional.of(ledgers)
                .map(ApiResponse::success);
    }

    @PostMapping("/excel")
    public ResponseEntity<byte[]> postLedgerExport(@RequestParam Integer year, @RequestParam Integer month) {
        return ledgerService.exportLedger(year, month)
                .map(data -> ResponseEntity.ok()
                        .headers(ledgerAttachmentFilename(year, month))
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(data))
                .orElseThrow();
    }

    @PutMapping("/{id}")
    public Optional<ApiResponse<LedgerDto>> putLedger(@PathVariable UUID id, @RequestBody LedgerDto body) {
        return ledgerService.updateLedgerById(id, body)
                .map(ApiResponse::success);
    }

    @DeleteMapping("/{id}")
    public void deleteLedger(@PathVariable UUID id) {
        ledgerService.deleteLedgerById(id);
    }

}
