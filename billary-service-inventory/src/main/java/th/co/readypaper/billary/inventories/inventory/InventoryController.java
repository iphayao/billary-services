package th.co.readypaper.billary.inventories.inventory;

import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.common.model.ApiResponse;
import th.co.readypaper.billary.inventories.inventory.model.dto.InventoryDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/inventories")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService, InventoryMapper inventoryMapper) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public Optional<ApiResponse<List<InventoryDto>>> getInventories() {
        return Optional.of(inventoryService.findAllInventory())
                .map(ApiResponse::success);
    }

    @GetMapping("/{id}")
    public Optional<ApiResponse<InventoryDto>> getInventory(@PathVariable UUID id) {
        return inventoryService.findInventoryById(id)
                .map(ApiResponse::success);
    }

    @GetMapping(params = {"product-id"})
    public Optional<ApiResponse<InventoryDto>> getInventoryProductId(@RequestParam("product-id") UUID productId) {
        return inventoryService.findInventoryByProductId(productId)
                .map(ApiResponse::success);
    }

    @PostMapping("/invoices")
    public Optional<ApiResponse<String>> postInventoryFromInvoice() {
        return inventoryService.buildInventoryFromInvoice()
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<String>> postInventory() {
        return inventoryService.buildInventory()
                .map(ApiResponse::success);
    }

}
