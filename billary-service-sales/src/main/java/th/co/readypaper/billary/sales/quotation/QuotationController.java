package th.co.readypaper.billary.sales.quotation;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.common.model.ApiResponse;
import th.co.readypaper.billary.common.model.DocumentId;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.sales.quotation.model.dto.QuotationDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static th.co.readypaper.billary.common.utils.HttpHeaderUtils.attachment;

@RestController
@RequestMapping("/quotations")
public class QuotationController {
    private final QuotationService quotationService;

    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @GetMapping("/document-id")
    public Optional<ApiResponse<DocumentId>> getDocumentId(@RequestParam(value = "publishedOn", required = false)
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate publishedOn) {
        return quotationService.generateDocumentId(publishedOn)
                .map(ApiResponse::success);
    }

    @GetMapping
    public Optional<ApiResponse<List<QuotationDto>>> getQuotation() {
        List<QuotationDto> quotations = quotationService.findAllQuotations();
        return Optional.of(quotations)
                .map(ApiResponse::success);
    }

    @GetMapping(params = {"page", "limit"})
    public Optional<ApiResponse<ResultPage<QuotationDto>>> getInvoices(@RequestParam Integer page,
                                                                       @RequestParam Integer limit,
                                                                       @RequestParam Map<String, Object> params) {
        ResultPage<QuotationDto> quotations = quotationService.findAllQuotations(page, limit, params);
        return Optional.of(quotations)
                .map(ApiResponse::success);
    }

    @GetMapping("/{id}")
    public Optional<ApiResponse<QuotationDto>> getQuotation(@PathVariable UUID id) {
        return quotationService.findAllQuotationById(id)
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<QuotationDto>> postQuotation(@RequestBody QuotationDto newQuotationDto) {
        return quotationService.creteNewQuotation(newQuotationDto)
                .map(ApiResponse::success);
    }

    @PutMapping("/{id}")
    public Optional<ApiResponse<QuotationDto>> putQuotation(@PathVariable UUID id, @RequestBody QuotationDto updateQuotation) {
        return quotationService.updateQuotationById(id, updateQuotation)
                .map(ApiResponse::success);
    }

    @DeleteMapping("/{id}")
    public void deleteQuotation(@PathVariable UUID id) {
        quotationService.deleteQuotation(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<byte[]> postQuotationDocument(@PathVariable UUID id,
                                                        @RequestParam(required = false, defaultValue = "false") boolean isSignAndStamp) {
        return quotationService.generateDocumentById(id, isSignAndStamp)
                .map(data -> ResponseEntity.ok()
                        .headers(attachment(id))
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(data))
                .orElseThrow();
    }

}
