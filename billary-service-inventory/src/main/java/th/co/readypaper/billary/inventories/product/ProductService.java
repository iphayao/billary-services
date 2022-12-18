package th.co.readypaper.billary.inventories.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.inventories.product.model.ProductDto;
import th.co.readypaper.billary.repo.entity.product.*;
import th.co.readypaper.billary.repo.repository.*;
import th.co.readypaper.billary.repo.repository.InventoryRepository;
import th.co.readypaper.billary.repo.entity.inventory.Inventory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductUnitRepository productUnitRepository;
    private final ProductVatTypeRepository productVatTypeRepository;
    private final ProductInventoryTypeRepository productInventoryTypeRepository;

    private final ProductMapper productMapper;

    private final InventoryRepository inventoryRepository;


    public ProductService(ProductRepository productRepository,
                          ProductTypeRepository productTypeRepository,
                          ProductCategoryRepository productCategoryRepository,
                          ProductUnitRepository productUnitRepository,
                          ProductVatTypeRepository productVatTypeRepository,
                          ProductInventoryTypeRepository productInventoryTypeRepository,
                          ProductMapper productMapper,
                          InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productUnitRepository = productUnitRepository;
        this.productVatTypeRepository = productVatTypeRepository;
        this.productInventoryTypeRepository = productInventoryTypeRepository;
        this.productMapper = productMapper;
        this.inventoryRepository = inventoryRepository;
    }

    public List<ProductDto> findAllProducts() {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "code")).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResultPage<ProductDto> findAllProducts(Integer page, Integer limit) {
        log.info("Find all product, page: {}, limit: {}", page, limit);
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "code"));

        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductDto> products = productPage.map(productMapper::toDto).toList();

        return ResultPage.of(products, page, limit, (int) productPage.getTotalElements());
    }

    public Optional<ProductDto> findProductById(UUID id) {
        return productRepository.findById(id)
                .map(productMapper::toDto);
    }

    public Optional<ProductDto> findProductByName(String name) {
        return productRepository.findByName(name)
                .map(productMapper::toDto);
    }

    public Optional<ProductDto> findProductByName(String name, String desc) {
        return productRepository.findByNameAndDescription(name, desc)
                .map(productMapper::toDto);
    }

    public Optional<ProductDto> createNewProduct(ProductDto newProduct) {
        log.info("Create new product name: {}", newProduct.getName());
        Product product = productRepository.save(productMapper.toEntity(newProduct));

        if (product.getInventoryType().getId() == 2) {
            Optional<Inventory> inventory = inventoryRepository.findByProductId(product.getId());
            if (inventory.isEmpty()) {
                log.info("Create product stock");
                inventoryRepository.save(buildInventory(product));
            }
        }

        return productRepository.findById(product.getId())
                .map(productMapper::toDto);
    }

    public Optional<ProductDto> updateProductById(UUID id, ProductDto updateProduct) {
        log.info("Update product ID: {}, name: {}", id, updateProduct.getName());
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product product = productRepository.save(productMapper.update(existingProduct.get(), updateProduct));

            if (existingProduct.get().getInventoryType().getId() != product.getInventoryType().getId()) {
                if (product.getInventoryType().getId() == 2) {
                    Optional<Inventory> inventory = inventoryRepository.findByProductId(product.getId());
                    if (inventory.isEmpty()) {
                        log.info("Create product stock");
                        inventoryRepository.save(buildInventory(product));
                    }
                }
            }

            return productRepository.findById(product.getId())
                    .map(productMapper::toDto);
        }

        return Optional.empty();
    }

    public void deleteProductById(UUID id) {
        log.info("Delete product ID: {}", id);
        productRepository.findById(id)
                .ifPresent(productRepository::delete);
    }

    public List<ProductType> findAllProductTypes() {
        log.info("Find all product types");
        return productTypeRepository.findAll();
    }

    public List<ProductCategory> findAllProductCategories() {
        log.info("Find all product categories");
        return productCategoryRepository.findAll();
    }

    public List<ProductUnit> findAllProductUnits() {
        log.info("Find all product units");
        return productUnitRepository.findAll();
    }

    public List<ProductVatType> findAllProductVatTypes() {
        log.info("Find all product VAT types");
        return productVatTypeRepository.findAll();
    }

    public List<ProductInventoryType> findAllProductInventoryTypes() {
        log.info("Find all product inventory types");
        return productInventoryTypeRepository.findAll();
    }

    private Inventory buildInventory(Product product) {
        Inventory inventory = new Inventory();
        inventory.setLocationId(1);
        inventory.setProduct(product);
        inventory.setInitialQty(0);
        inventory.setInitialPrice(product.getPrice());
        inventory.setCurrentQty(0);
        inventory.setCurrentPrice(product.getPrice());
        log.info("Create Inventory for Product ID: {}", product.getId());
        return inventory;
    }

    public List<ProductDto> createInventory() {
        return productRepository.findAll().stream()
                .filter(product -> product.getInventoryType().getId() == 2)
                .map(product -> {
                    Optional<Inventory> inventory = inventoryRepository.findByProductId(product.getId());
                    if (inventory.isPresent()) {
                        log.info("Inventory ID: {} (Product: {}) exist", inventory.get().getId(), product.getName());
                    } else {
                        log.info("build inventory for product : {}", product.getName());
                        Inventory saved = inventoryRepository.save(buildInventory(product));
                        log.info("inventory : {}", saved.getId());
                    }
                    return product;
                })
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
}
