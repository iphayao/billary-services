package th.co.readypaper.billary.accounting.voucher;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.accounting.voucher.model.JournalVoucherDto;
import th.co.readypaper.billary.common.model.ApiResponse;
import th.co.readypaper.billary.common.model.DocumentId;
import th.co.readypaper.billary.common.model.ResultPage;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/journal-vouchers")
public class JournalVoucherController {
    private final JournalVoucherService journalVoucherService;

    public JournalVoucherController(JournalVoucherService journalVoucherService) {
        this.journalVoucherService = journalVoucherService;
    }

    @GetMapping("/document-id")
    public Optional<ApiResponse<DocumentId>> getDocumentId(@RequestParam(value = "publishedOn", required = false)
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishedOn) {
        return journalVoucherService.generateDocumentId(publishedOn)
                .map(ApiResponse::success);
    }

    @GetMapping
    public Optional<ApiResponse<ResultPage<JournalVoucherDto>>> getJournalVouchers(@RequestParam Integer page,
                                                                                  @RequestParam Integer limit,
                                                                                  @RequestParam Map<String, Object> params) {
        var journalVouchers = journalVoucherService.findJournalVouchers(page, limit, params);
        return Optional.of(journalVouchers)
                .map(ApiResponse::success);
    }

    @GetMapping("/{id}")
    public Optional<ApiResponse<JournalVoucherDto>> getJournalVoucher(@PathVariable UUID id) {
        return journalVoucherService.findJournalVoucherById(id)
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<JournalVoucherDto>> postJournalVoucher(@RequestBody JournalVoucherDto body) {
        return journalVoucherService.createJournalVoucher(body)
                .map(ApiResponse::success);
    }

    @PutMapping("/{id}")
    public Optional<ApiResponse<JournalVoucherDto>> putJournalEntry(@PathVariable UUID id, @RequestBody JournalVoucherDto body) {
        return journalVoucherService.updateJournalVoucherById(id, body)
                .map(ApiResponse::success);
    }

}
