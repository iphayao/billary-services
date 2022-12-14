package th.co.readypaper.billary.sales.invoice;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.common.model.ApiResponse;
import th.co.readypaper.billary.common.model.DocumentId;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.sales.common.model.document.DocumentType;
import th.co.readypaper.billary.sales.invoice.model.dto.InvoiceDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static th.co.readypaper.billary.common.utils.HttpHeaderUtils.attachment;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/document-id")
    public Optional<ApiResponse<DocumentId>> getDocumentId(@RequestParam(value = "publishedOn", required = false)
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate publishedOn) {
        return invoiceService.generateDocumentId(publishedOn)
                .map(ApiResponse::success);
    }

    @GetMapping
    public Optional<ApiResponse<List<InvoiceDto>>> getInvoices() {
        List<InvoiceDto> invoices = invoiceService.findAllInvoices();
        return Optional.of(invoices)
                .map(ApiResponse::success);
    }

    @GetMapping(params = {"page", "limit"})
    public Optional<ApiResponse<ResultPage<InvoiceDto>>> getInvoices(@RequestParam Integer page,
                                                                     @RequestParam Integer limit,
                                                                     @RequestParam Map<String, Object> params) {
        ResultPage<InvoiceDto> invoices = invoiceService.findAllInvoices(page, limit, params);
        return Optional.of(invoices)
                .map(ApiResponse::success);
    }

    @GetMapping("/{id}")
    public Optional<ApiResponse<InvoiceDto>> getInvoice(@PathVariable UUID id) {
        return invoiceService.findInvoiceById(id)
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<InvoiceDto>> postInvoice(@RequestBody InvoiceDto newInvoiceDto) {
        return invoiceService.createNewInvoice(newInvoiceDto)
                .map(ApiResponse::success);
    }

    @PutMapping("/{id}")
    public Optional<ApiResponse<InvoiceDto>> putInvoice(@PathVariable UUID id, @RequestBody InvoiceDto updateInvoiceDto) {
        return invoiceService.updateInvoiceById(id, updateInvoiceDto)
                .map(ApiResponse::success);
    }

    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable UUID id) {
        invoiceService.deleteInvoiceById(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<byte[]> postInvoiceDocument(@PathVariable UUID id,
                                                      @RequestParam(required = false, defaultValue = "FULL") DocumentType type,
                                                      @RequestParam(required = false, defaultValue = "false") boolean isSignAndStamp,
                                                      @RequestParam(required = false, defaultValue = "false") boolean isDeliveryNoteAndInvoice) {
     return invoiceService.generateDocumentById(id, type, isSignAndStamp, isDeliveryNoteAndInvoice)
             .map(data -> ResponseEntity.ok()
                     .headers(attachment(id))
                     .contentType(MediaType.APPLICATION_PDF)
                     .body(data))
             .orElseThrow();
    }

}
