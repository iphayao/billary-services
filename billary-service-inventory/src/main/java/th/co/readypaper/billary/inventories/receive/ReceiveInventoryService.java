package th.co.readypaper.billary.inventories.receive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.common.service.DocumentIdBaseService;
import th.co.readypaper.billary.inventories.receive.model.dto.ReceiveInventoryDto;
import th.co.readypaper.billary.repo.entity.receive.ReceiveInventory;
import th.co.readypaper.billary.repo.entity.receive.ReceiveInventoryLineItem;
import th.co.readypaper.billary.repo.repository.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReceiveInventoryService extends DocumentIdBaseService {
    private final ReceiveInventoryRepository receiveInventoryRepository;
    private final ContactRepository contactRepository;
    private final ProductRepository productRepository;
    private final ReceiveInventoryMapper receiveInventoryMapper;

    public ReceiveInventoryService(ReceiveInventoryRepository receiveInventoryRepository,
                                   CompanyRepository companyRepository,
                                   DocumentRepository documentRepository,
                                   DocumentSerialRepository documentSerialRepository,
                                   ContactRepository contactRepository,
                                   ProductRepository productRepository,
                                   ReceiveInventoryMapper receiveInventoryMapper) {
        super(companyRepository, documentRepository, documentSerialRepository, 6);
        this.receiveInventoryRepository = receiveInventoryRepository;
        this.contactRepository = contactRepository;
        this.productRepository = productRepository;
        this.receiveInventoryMapper = receiveInventoryMapper;
    }


    public List<ReceiveInventoryDto> findAllPurchase() {
        log.info("Find all receive inventories");
        return receiveInventoryRepository.findByOrderByDocumentIdDesc().stream()
                .map(receiveInventoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResultPage<ReceiveInventoryDto> findAllPurchase(Integer page, Integer limit, Map<String, Object> params) {
        log.info("Find all receive inventories, page: {}, limit: {}, params: {}", page, limit, params);
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "documentId"));

        Page<ReceiveInventory> result = receiveInventoryRepository.findAll(pageable);
        List<ReceiveInventoryDto> res = result.map(receiveInventoryMapper::toDto).toList();

        return ResultPage.of(res, page, limit, (int) result.getTotalElements());
    }

    public Optional<ReceiveInventoryDto> findById(UUID id) {
        return receiveInventoryRepository.findById(id)
                .map(receiveInventoryMapper::toDto);
    }

    public Optional<ReceiveInventoryDto> createNewPurchase(ReceiveInventoryDto newReceiveInventoryDto) {
        log.info("Create new receive inventory document ID: {}", newReceiveInventoryDto.getDocumentId());

        ReceiveInventory newReceiveInventoryEntity = receiveInventoryMapper.toEntity(newReceiveInventoryDto);
        newReceiveInventoryEntity.setStatusId(2);

        contactRepository.findById(newReceiveInventoryEntity.getContact().getId())
                .ifPresent(newReceiveInventoryEntity::setContact);

        newReceiveInventoryEntity.setLineItems(
                newReceiveInventoryEntity.getLineItems().stream()
                        .map(this::getProductEntity)
                        .collect(Collectors.toList()));

        ReceiveInventory savedReceiveInventoryEntity = receiveInventoryRepository.save(newReceiveInventoryEntity);
        updateDocumentId(savedReceiveInventoryEntity.getDocumentId());

        return Optional.of(savedReceiveInventoryEntity)
                .map(receiveInventoryMapper::toDto);
    }

    public Optional<ReceiveInventoryDto> updatePurchaseById(UUID id, ReceiveInventoryDto updatePurchase) {
        log.info("Update receive inventory document ID: {}", id);
        ReceiveInventory updatedReceiveInventoryEntity = receiveInventoryMapper.toEntity(updatePurchase);
        return receiveInventoryRepository.findById(id)
                .map(receiveInventory -> {
                    ReceiveInventory mappedReceiveInventory = receiveInventoryMapper.update(receiveInventory, updatedReceiveInventoryEntity);
                    mappedReceiveInventory.setLineItems(
                            mappedReceiveInventory.getLineItems().stream()
                                    .map(this::getProductEntity)
                                    .collect(Collectors.toList()));

                    return receiveInventoryRepository.save(mappedReceiveInventory);
                })
                .map(receiveInventoryMapper::toDto);
    }

    private ReceiveInventoryLineItem getProductEntity(ReceiveInventoryLineItem lineItem) {
        productRepository.findById(lineItem.getProduct().getId())
                .ifPresent(lineItem::setProduct);
        return lineItem;
    }

    public void deletePurchaseById(UUID id) {
        receiveInventoryRepository.findById(id)
                .ifPresent(receiveInventoryRepository::delete);
    }
}
