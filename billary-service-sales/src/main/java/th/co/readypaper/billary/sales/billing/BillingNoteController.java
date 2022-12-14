package th.co.readypaper.billary.sales.billing;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.common.model.ApiResponse;
import th.co.readypaper.billary.common.model.DocumentId;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.sales.billing.model.dto.BillingNoteDto;
import th.co.readypaper.billary.sales.common.model.document.DocumentType;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static th.co.readypaper.billary.common.utils.HttpHeaderUtils.attachment;

@RestController
@RequestMapping("/billing-notes")
public class BillingNoteController {
    private final BillingNoteService billingNoteService;

    public BillingNoteController(BillingNoteService billingNoteService) {
        this.billingNoteService = billingNoteService;
    }

    @GetMapping("/document-id")
    public Optional<ApiResponse<DocumentId>> getDocumentId(@RequestParam(value = "publishedOn", required = false)
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate publishedOn) {
        return billingNoteService.generateDocumentId(publishedOn)
                .map(ApiResponse::success);
    }

    @GetMapping(params = {"page", "limit"})
    public Optional<ApiResponse<ResultPage<BillingNoteDto>>> getBillingNotes(@RequestParam Integer page,
                                                                             @RequestParam Integer limit,
                                                                             @RequestParam Map<String, Object> params) {
        ResultPage<BillingNoteDto> billingNotes = billingNoteService.findAllBillingNotes(page, limit, params);
        return Optional.of(billingNotes)
                .map(ApiResponse::success);
    }

    @GetMapping("/{id}")
    public Optional<ApiResponse<BillingNoteDto>> getBillingNote(@PathVariable UUID id) {
        return billingNoteService.findBillingNoteById(id)
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<BillingNoteDto>> postBillingNote(@RequestBody BillingNoteDto newBillingNote) {
        return billingNoteService.createNewBillingNote(newBillingNote)
                .map(ApiResponse::success);
    }

    @PutMapping("/{id}")
    public Optional<ApiResponse<BillingNoteDto>> putBillingNote(@PathVariable UUID id, @RequestBody BillingNoteDto updateBillingNote) {
        return billingNoteService.updateBillingNoteById(id, updateBillingNote)
                .map(ApiResponse::success);
    }

    @DeleteMapping("/{id}")
    public void deleteBillingNote(@PathVariable UUID id) {
        billingNoteService.deleteBillingNoteById(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<byte[]> postBillingNoteDocument(@PathVariable UUID id,
                                                          @RequestParam(required = false, defaultValue = "FULL") DocumentType docType,
                                                          @RequestParam(required = false, defaultValue = "false") boolean isSignAndStamp) {
        return billingNoteService.generateDocumentById(id, docType, isSignAndStamp)
                .map(data -> ResponseEntity.ok()
                        .headers(attachment(id))
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(data))
                .orElseThrow();
    }

}
