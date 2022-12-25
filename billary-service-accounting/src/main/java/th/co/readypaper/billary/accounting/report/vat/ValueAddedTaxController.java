package th.co.readypaper.billary.accounting.report.vat;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.accounting.report.vat.model.ValueAddedTaxReport;
import th.co.readypaper.billary.common.model.ApiResponse;

import java.util.Optional;

import static th.co.readypaper.billary.common.utils.FileNameUtils.attachment;

@RestController
@RequestMapping("/report/vat")
public class ValueAddedTaxController {
    private final ValueAddedTaxService valueAddedTaxService;

    public ValueAddedTaxController(ValueAddedTaxService valueAddedTaxService) {
        this.valueAddedTaxService = valueAddedTaxService;
    }

    @GetMapping("/output-tax")
    public Optional<ApiResponse<ValueAddedTaxReport>> getOutputTax(@RequestParam Integer year,
                                                                   @RequestParam Integer month) {
        return valueAddedTaxService.findOutputTax(year, month)
                .map(ApiResponse::success);
    }

    @PostMapping("/output-tax")
    public ResponseEntity<byte[]> exportOutputTax(@RequestParam Integer year,
                                                  @RequestParam Integer month) {
        return valueAddedTaxService.generateOutputTax(year, month)
                .map(data -> ResponseEntity.ok()
                        .headers(attachment("ภาษีมูลค่าเพิ่ม-ภาษีขาย", year, month))
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(data))
                .orElseThrow();
    }

    @GetMapping("/input-tax")
    public Optional<ApiResponse<ValueAddedTaxReport>> getInputTax(@RequestParam Integer year,
                                                                  @RequestParam Integer month) {
        return valueAddedTaxService.findInputTax(year, month)
                .map(ApiResponse::success);
    }

    @PostMapping("/input-tax")
    public ResponseEntity<byte[]> exportInputTax(@RequestParam Integer year,
                                                 @RequestParam Integer month) {
        return valueAddedTaxService.generateInputTax(year, month)
                .map(data -> ResponseEntity.ok()
                        .headers(attachment("ภาษีมูลค่าเพิ่ม-ภาษีซื้อ", year, month))
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(data))
                .orElseThrow();

    }

}
