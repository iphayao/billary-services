package th.co.readypaper.billary.sales.receipt;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.common.model.ApiResponse;
import th.co.readypaper.billary.common.model.DocumentId;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.sales.common.model.document.DocumentType;
import th.co.readypaper.billary.sales.receipt.model.dto.ReceiptDto;
import th.co.readypaper.billary.sales.receipt.model.dto.ReceiptPaymentTypeDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static th.co.readypaper.billary.common.utils.HttpHeaderUtils.attachment;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/document-id")
    public Optional<ApiResponse<DocumentId>> getDocumentId(@RequestParam(value = "publishedOn", required = false)
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate publishedOn) {
        return receiptService.generateDocumentId(publishedOn)
                .map(ApiResponse::success);
    }

    @GetMapping(params = {"page", "limit"})
    public Optional<ApiResponse<ResultPage<ReceiptDto>>> getReceipts(@RequestParam Integer page,
                                                                     @RequestParam Integer limit,
                                                                     @RequestParam Map<String, Object> params) {
        ResultPage<ReceiptDto> receipts = receiptService.findAllReceipts(page, limit, params);
        return Optional.of(receipts)
                .map(ApiResponse::success);
    }

    @GetMapping("/{id}")
    public Optional<ApiResponse<ReceiptDto>> getReceipt(@PathVariable UUID id) {
        return receiptService.findReceiptById(id)
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<ReceiptDto>> postReceipt(@RequestBody ReceiptDto newReceiptDto) {
        return receiptService.createNewReceipt(newReceiptDto)
                .map(ApiResponse::success);
    }

    @PutMapping("/{id}")
    public Optional<ApiResponse<ReceiptDto>> putReceipt(@PathVariable UUID id, @RequestBody ReceiptDto updateReceiptDto) {
        return receiptService.updateReceiptById(id, updateReceiptDto)
                .map(ApiResponse::success);
    }

    @DeleteMapping("/{id}")
    public void deleteReceipt(@PathVariable UUID id) {
        receiptService.deleteReceiptById(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<byte[]> postReceiptDocument(@PathVariable UUID id,
                                                      @RequestParam(required = false, defaultValue = "FULL") DocumentType type,
                                                      @RequestParam(required = false, defaultValue = "false") boolean isSignAndStamp) {
        return receiptService.generateDocumentById(id, type, isSignAndStamp)
                .map(data -> ResponseEntity.ok()
                        .headers(attachment(id))
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(data))
                .orElseThrow();
    }

    @GetMapping("/payment-types")
    public Optional<ApiResponse<List<ReceiptPaymentTypeDto>>> getPaymentType() {
        List<ReceiptPaymentTypeDto> paymentTypes = receiptService.getPaymentType();
        return Optional.of(paymentTypes)
                .map(ApiResponse::success);
    }

}
