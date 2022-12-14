package th.co.readypaper.billary.accounting.tax;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import th.co.readypaper.billary.accounting.tax.model.WithholdingTaxReport;
import th.co.readypaper.billary.common.model.ApiResponse;

import java.util.Optional;

@RestController
@RequestMapping("/withholding-tax")
public class WithholdingTaxController {
    private final WithholdingTaxService withholdingTaxService;

    public WithholdingTaxController(WithholdingTaxService withholdingTaxService) {
        this.withholdingTaxService = withholdingTaxService;
    }

    @GetMapping
    public Optional<ApiResponse<WithholdingTaxReport>> getWithholdingTax(@RequestParam Integer year,
                                                                         @RequestParam Integer month) {
        return withholdingTaxService.findWithholdingTax(year, month)
                .map(ApiResponse::success);
    }

}
