package th.co.readypaper.billary.ompanies.company;

import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.common.model.ApiResponse;
import th.co.readypaper.billary.ompanies.company.model.CompanyDto;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/{companyCode}")
    public Optional<ApiResponse<CompanyDto>> getCompanyByCompanyCode(@PathVariable String companyCode) {
        return companyService.findByCompanyCode(companyCode)
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<CompanyDto>> postCompany(@RequestBody CompanyDto newCompanyDto) {
        return companyService.createNewCompany(newCompanyDto)
                .map(ApiResponse::success);
    }

    @PostMapping("/{id}")
    public Optional<ApiResponse<CompanyDto>> postCompanyImage(@PathVariable UUID id) {
        return companyService.createCompanyImage(id)
                .map(ApiResponse::success);
    }

}
