package th.co.readypaper.billary.inventories.receive;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.common.model.ApiResponse;
import th.co.readypaper.billary.common.model.DocumentId;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.inventories.receive.model.dto.ReceiveInventoryDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/receive-inventories")
public class ReceiveInventoryController {
    private final ReceiveInventoryService receiveInventoryService;

    public ReceiveInventoryController(ReceiveInventoryService receiveInventoryService) {
        this.receiveInventoryService = receiveInventoryService;
    }

    @GetMapping("/document-id")
    public Optional<ApiResponse<DocumentId>> getDocumentId(@RequestParam(value = "publishedOn", required = false)
                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate publishedOn) {
        return receiveInventoryService.generateDocumentId(publishedOn)
                .map(ApiResponse::success);
    }

    @GetMapping
    public Optional<ApiResponse<List<ReceiveInventoryDto>>> getPurchases() {
        List<ReceiveInventoryDto> purchases = receiveInventoryService.findAllPurchase();
        return Optional.of(purchases)
                .map(ApiResponse::success);
    }

    @GetMapping(params = {"page", "limit"})
    public Optional<ApiResponse<ResultPage<ReceiveInventoryDto>>> getPurchases(@RequestParam Integer page,
                                                                               @RequestParam Integer limit,
                                                                               @RequestParam Map<String, Object> params) {
        ResultPage<ReceiveInventoryDto> purchases = receiveInventoryService.findAllPurchase(page, limit, params);
        return Optional.of(purchases)
                .map(ApiResponse::success);
    }

    @GetMapping("/{id}")
    public Optional<ApiResponse<ReceiveInventoryDto>> getPurchase(@PathVariable UUID id) {
        return receiveInventoryService.findById(id)
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<ReceiveInventoryDto>> postPurchases(@RequestBody ReceiveInventoryDto newPurchase) {
        return receiveInventoryService.createNewPurchase(newPurchase)
                .map(ApiResponse::success);
    }

    @PutMapping("/{id}")
    public Optional<ApiResponse<ReceiveInventoryDto>> putPurchase(@PathVariable UUID id, @RequestBody ReceiveInventoryDto updatePurchase) {
        return receiveInventoryService.updatePurchaseById(id, updatePurchase)
                .map(ApiResponse::success);
    }

    @DeleteMapping("/{id}")
    public void deletePurchase(@PathVariable UUID id) {
        receiveInventoryService.deletePurchaseById(id);
    }

}
