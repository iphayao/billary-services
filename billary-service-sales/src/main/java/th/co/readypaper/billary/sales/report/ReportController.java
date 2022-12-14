package th.co.readypaper.billary.sales.report;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static th.co.readypaper.billary.common.utils.FileNameUtils.attachment;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/inventory")
    public ResponseEntity<byte[]> genInventoryReport() {// @RequestParam("product-id") UUID productId) {
        return reportService.generateInventoryReport()
                .map(data -> ResponseEntity.ok()
                        .headers(attachment("inventory"))
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(data))
                .orElse(ResponseEntity.ok("".getBytes()));
    }

}
