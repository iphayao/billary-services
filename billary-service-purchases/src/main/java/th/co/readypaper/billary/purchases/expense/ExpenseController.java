package th.co.readypaper.billary.purchases.expense;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.common.model.ApiResponse;
import th.co.readypaper.billary.common.model.DocumentId;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.common.model.dto.expense.ExpenseDto;
import th.co.readypaper.billary.common.model.dto.expense.ExpenseVatTypeDto;
import th.co.readypaper.billary.common.model.dto.expense.WithholdingTaxPercentDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        log.info("Hello");
        this.expenseService = expenseService;
    }

    @GetMapping("/document-id")
    public Optional<ApiResponse<DocumentId>> getDocumentId(@RequestParam(value = "publishedOn", required = false)
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishedOn) {
        return expenseService.generateDocumentId(publishedOn)
                .map(ApiResponse::success);
    }

    @GetMapping
    public Optional<ApiResponse<ResultPage<ExpenseDto>>> getExpenses(@RequestParam Integer page,
                                                                     @RequestParam Integer limit,
                                                                     @RequestParam Map<String, Object> params) {
        ResultPage<ExpenseDto> expenses = expenseService.findAllExpenses(page, limit, params);
        return Optional.of(expenses)
                .map(ApiResponse::success);
    }

    @GetMapping("/{id}")
    public Optional<ApiResponse<ExpenseDto>> getExpense(@PathVariable UUID id) {
        return expenseService.findExpenseById(id)
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<ExpenseDto>> postExpense(@RequestBody ExpenseDto newExpenseDto) {
        return expenseService.createNewExpense(newExpenseDto)
                .map(ApiResponse::success);
    }

    @PutMapping("/{id}")
    public Optional<ApiResponse<ExpenseDto>> putExpense(@PathVariable UUID id, @RequestBody ExpenseDto updateExpenseDto) {
        return expenseService.updateExpenseById(id, updateExpenseDto)
                .map(ApiResponse::success);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable UUID id) {
        expenseService.deleteExpenseById(id);
    }

    @GetMapping("/vat-types")
    public Optional<ApiResponse<List<ExpenseVatTypeDto>>> getExpenseVatType() {
        List<ExpenseVatTypeDto> expenseVatTypes = expenseService.findAllProductVatType();
        return Optional.of(expenseVatTypes)
                .map(ApiResponse::success);
    }

    @GetMapping("/withholding-tax-percents")
    public Optional<ApiResponse<List<WithholdingTaxPercentDto>>> getWithholdingTaxPercent() {
        List<WithholdingTaxPercentDto> withholdingTaxPercents = expenseService.findAllWithholdingTaxPercent();
        return Optional.of(withholdingTaxPercents)
                .map(ApiResponse::success);
    }

}
