package th.co.readypaper.billary.accounting.report.journal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.accounting.common.model.AccountingYearlySummary;
import th.co.readypaper.billary.accounting.report.journal.model.GeneralJournalDto;
import th.co.readypaper.billary.common.model.ApiResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static th.co.readypaper.billary.common.utils.HttpHeaderUtils.journalAttachmentFilename;

@RestController
@RequestMapping("/general-journals")
public class GeneralJournalController {
    private final GeneralJournalService generalJournalService;

    public GeneralJournalController(GeneralJournalService generalJournalService) {
        this.generalJournalService = generalJournalService;
    }

    @GetMapping
    public Optional<ApiResponse<List<GeneralJournalDto>>> getGeneralJournals(@RequestParam(required = false) Integer year,
                                                                             @RequestParam(required = false) Integer month) {
        var generalJournals = generalJournalService.findAllGeneralJournal(year, month);
        return Optional.of(generalJournals)
                .map(ApiResponse::success);
    }

    @GetMapping("/summary")
    public Optional<ApiResponse<List<AccountingYearlySummary>>> getGeneralJournalSummary(@RequestParam Integer year) {
        var generalJournalYearlySummary = generalJournalService.findGeneralJournalYearlySummary(year);
        return Optional.of(generalJournalYearlySummary)
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<List<GeneralJournalDto>>> postGeneralJournal(@RequestParam Integer year,
                                                                             @RequestParam(required = false) Integer month,
                                                                             @RequestParam(required = false) Integer day) {
        var generalJournals = generalJournalService.createGeneralJournal(year, month, day);
        return Optional.of(generalJournals)
                .map(ApiResponse::success);
    }

    @PostMapping("/excel")
    public ResponseEntity<byte[]> postGeneralJournalExport(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate date,
                                                           @RequestParam(required = false) int year, int month) {
        return generalJournalService.exportGeneralJournal(year, month)
                .map(data -> ResponseEntity.ok()
                        .headers(journalAttachmentFilename(year, month))
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(data))
                .orElseThrow();

    }

}
