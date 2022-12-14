package th.co.readypaper.billary.inventories.inventory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.inventories.inventory.model.dto.InventoryDto;
import th.co.readypaper.billary.repo.entity.invoice.Invoice;
import th.co.readypaper.billary.repo.entity.product.Product;
import th.co.readypaper.billary.repo.entity.inventory.Inventory;
import th.co.readypaper.billary.repo.entity.inventory.InventoryMovement;
import th.co.readypaper.billary.repo.entity.inventory.InventoryMovementType;
import th.co.readypaper.billary.repo.repository.InventoryMovementRepository;
import th.co.readypaper.billary.repo.repository.InventoryRepository;
import th.co.readypaper.billary.repo.repository.InvoiceRepository;
import th.co.readypaper.billary.repo.repository.ReceiveInventoryRepository;
import th.co.readypaper.billary.repo.entity.receive.ReceiveInventory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final InvoiceRepository invoiceRepository;
    private final ReceiveInventoryRepository receiveInventoryRepository;

    private final InventoryMapper inventoryMapper;

    public InventoryService(InventoryRepository inventoryRepository,
                            InvoiceRepository invoiceRepository,
                            InventoryMovementRepository inventoryMovementRepository,
                            ReceiveInventoryRepository receiveInventoryRepository,
                            InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.invoiceRepository = invoiceRepository;
        this.inventoryMovementRepository = inventoryMovementRepository;
        this.receiveInventoryRepository = receiveInventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    public List<InventoryDto> findAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(inventoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<InventoryDto> findInventoryById(UUID id) {
        return inventoryRepository.findById(id)
                .map(inventoryMapper::toDto);
    }

    public Optional<InventoryDto> findInventoryByProductId(UUID id) {
        return inventoryRepository.findByProductId(id)
                .map(inventoryMapper::toDto);
    }

    public Optional<String> buildInventoryFromInvoice() {
        invoiceRepository.findByOrderByDocumentId()
                .forEach(invoice -> {
                    log.info("Invoice ID: {}", invoice.getDocumentId());
                    invoice.getLineItems()
                            .forEach(lineItem -> {
                                if (lineItem.getProduct().getInventoryType().getId() == 2) {
                                    inventoryRepository.findByProductId(lineItem.getProduct().getId())
                                            .ifPresent(inventory -> {
                                                int remaining = inventory.getCurrentQty() - lineItem.getQuantity();
                                                BigDecimal averageUnitPrice = lineItem.getLineAmount().divide(BigDecimal.valueOf(lineItem.getQuantity()), RoundingMode.CEILING);
                                                InventoryMovement inventoryMovement = InventoryMovement.builder()
                                                        .reference(invoice.getDocumentId())
                                                        .movementTypeId(InventoryMovementType.REMOVE.getVal())
                                                        .inventoryId(inventory.getId())
                                                        .date(invoice.getIssuedDate())
                                                        .quantity(lineItem.getQuantity())
                                                        .remaining(remaining)
                                                        .unitPrice(averageUnitPrice)
                                                        .totalAmount(lineItem.getLineAmount())
                                                        .build();
                                                inventoryMovementRepository.save(inventoryMovement);

                                                inventory.setCurrentPrice(lineItem.getUnitPrice());
                                                inventory.setCurrentQty(remaining);
                                                inventoryRepository.save(inventory);
                                            });
                                }
                            });
                });

        return Optional.of("Done!");

    }

    public Optional<String> buildInventory() {

        Map<String, Object> documents = new TreeMap<>();

        invoiceRepository.findByOrderByDocumentId()
                .forEach(invoice -> {
                    String key = invoice.getIssuedDate().toEpochDay() + invoice.getDocumentId();
                    documents.put(key, invoice);
                });
        receiveInventoryRepository.findByOrderByDocumentId()
                .forEach(receiveInventory -> {
                    String key = receiveInventory.getIssuedDate().toEpochDay() + receiveInventory.getDocumentId();
                    documents.put(key, receiveInventory);
                });

        log.info("Size : {}", documents.size());

        documents.forEach((k, v) -> {
            if (v instanceof Invoice invoice) {
                log.info("Invoice ID: {} / {} ({})", invoice.getDocumentId(), k, invoice.getIssuedDate());
                invoice.getLineItems()
                        .forEach(lineItem -> {
                            if (lineItem.getProduct().getInventoryType().getId() == 2) {
                                inventoryRepository.findByProductId(lineItem.getProduct().getId())
                                        .ifPresent(inventory -> {
                                            int remaining = inventory.getCurrentQty() - lineItem.getQuantity();
                                            BigDecimal averageUnitPrice = lineItem.getLineAmount().divide(BigDecimal.valueOf(lineItem.getQuantity()), RoundingMode.CEILING);
                                            InventoryMovement inventoryMovement = InventoryMovement.builder()
                                                    .reference(invoice.getDocumentId())
                                                    .movementTypeId(InventoryMovementType.REMOVE.getVal())
                                                    .inventoryId(inventory.getId())
                                                    .date(invoice.getIssuedDate())
                                                    .quantity(lineItem.getQuantity())
                                                    .remaining(remaining)
                                                    .unitPrice(averageUnitPrice)
                                                    .totalAmount(lineItem.getLineAmount())
                                                    .build();
                                            inventoryMovementRepository.save(inventoryMovement);

                                            inventory.setCurrentPrice(lineItem.getUnitPrice());
                                            inventory.setCurrentQty(remaining);
                                            inventoryRepository.save(inventory);
                                        });
                            }
                        });
            } else if (v instanceof ReceiveInventory receiveInventory) {
                log.info("Receive Inventory ID: {} / {} ({})", receiveInventory.getDocumentId(), k, receiveInventory.getIssuedDate());
                receiveInventory.getLineItems()
                        .forEach(lineItem -> {
                            inventoryRepository.findByProductId(lineItem.getProduct().getId())
                                    .ifPresent(inventory -> {
                                        int remaining = inventory.getCurrentQty() + lineItem.getQuantity();
                                        BigDecimal averageUnitPrice = lineItem.getLineAmount().divide(BigDecimal.valueOf(lineItem.getQuantity()), RoundingMode.CEILING);
                                        InventoryMovement inventoryMovement = InventoryMovement.builder()
                                                .reference(receiveInventory.getDocumentId())
                                                .movementTypeId(InventoryMovementType.ADD.getVal())
                                                .inventoryId(inventory.getId())
                                                .date(receiveInventory.getIssuedDate())
                                                .quantity(lineItem.getQuantity())
                                                .remaining(remaining)
                                                .unitPrice(averageUnitPrice)
                                                .totalAmount(lineItem.getLineAmount())
                                                .build();
                                        inventoryMovementRepository.save(inventoryMovement);

                                        inventory.setCurrentPrice(lineItem.getUnitPrice());
                                        inventory.setCurrentQty(remaining);
                                        inventoryRepository.save(inventory);
                                    });
                        });
            }
        });

        return Optional.of("Done!");
    }

    public Optional<Inventory> createNewInventory(Product product) {
        Inventory inventory = new Inventory();
        inventory.setLocationId(1);
        inventory.setProduct(product);
        inventory.setInitialQty(0);
        inventory.setInitialPrice(product.getPrice());
        inventory.setCurrentQty(0);
        inventory.setCurrentPrice(product.getPrice());
        log.info("Create Inventory for Product ID: {}", product.getId());
        return Optional.of(inventoryRepository.save(inventory));
    }

}
