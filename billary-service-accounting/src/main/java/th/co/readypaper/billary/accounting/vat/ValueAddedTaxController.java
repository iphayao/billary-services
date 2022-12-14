package th.co.readypaper.billary.accounting.vat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import th.co.readypaper.billary.accounting.vat.model.ValueAddedTaxReport;
import th.co.readypaper.billary.common.model.ApiResponse;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/vat")
public class ValueAddedTaxController {
    private final ValueAddedTaxService valueAddedTaxService;

    public ValueAddedTaxController(ValueAddedTaxService valueAddedTaxService) {
        log.info("Hello");
        this.valueAddedTaxService = valueAddedTaxService;
    }

    @GetMapping("/output-tax")
    public Optional<ApiResponse<ValueAddedTaxReport>> getOutputTax(@RequestParam Integer year,
                                                                   @RequestParam Integer month) {
        return valueAddedTaxService.findOutputTax(year, month)
                .map(ApiResponse::success);
    }

    @GetMapping("/input-tax")
    public Optional<ApiResponse<ValueAddedTaxReport>> getInputTax(@RequestParam Integer year,
                                                                  @RequestParam Integer month) {
        return valueAddedTaxService.findInputTax(year, month)
                .map(ApiResponse::success);
    }
}
