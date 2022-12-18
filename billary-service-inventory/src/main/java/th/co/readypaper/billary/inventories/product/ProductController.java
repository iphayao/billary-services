package th.co.readypaper.billary.inventories.product;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.common.model.ApiResponse;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.inventories.product.model.ProductDto;
import th.co.readypaper.billary.repo.entity.product.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
    }

    @GetMapping
    public Optional<ApiResponse<List<ProductDto>>> getProducts() {
        List<ProductDto> products = productService.findAllProducts();
        return Optional.of(products)
                .map(ApiResponse::success);
    }

    @GetMapping(params = {"page", "limit"})
    public Optional<ApiResponse<ResultPage<ProductDto>>> getProducts(@RequestParam Integer page,
                                                                     @RequestParam Integer limit) {
        ResultPage<ProductDto> products = productService.findAllProducts(page, limit);
        return Optional.of(products)
                .map(ApiResponse::success);
    }

    @GetMapping("/{id}")
    public Optional<ApiResponse<ProductDto>> getProduct(@PathVariable UUID id) {
        return productService.findProductById(id)
                .map(ApiResponse::success);
    }

    @GetMapping(params = {"name"})
    public Optional<ApiResponse<ProductDto>> getProduct(@RequestParam String name) {
        return productService.findProductByName(name)
                .map(ApiResponse::success);
    }

    @GetMapping(params = {"name", "desc"})
    public Optional<ApiResponse<ProductDto>> getProduct(@RequestParam String name,
                                                        @RequestParam String desc) {
        return productService.findProductByName(name, desc)
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<ProductDto>> postProduct(@RequestBody ProductDto reqBody) {
        return productService.createNewProduct(reqBody)
                .map(ApiResponse::success);
    }

    @PostMapping("/genInventory")
    public Optional<ApiResponse<List<ProductDto>>> postProduct() {
        List<ProductDto> products = productService.createInventory();
        return Optional.of(products)
                .map(ApiResponse::success);
    }

    @PutMapping("/{id}")
    public Optional<ApiResponse<ProductDto>> putProduct(@PathVariable UUID id, @RequestBody ProductDto reqBody) {
        return productService.updateProductById(id, reqBody)
                .map(ApiResponse::success);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable UUID id) {
        productService.deleteProductById(id);
    }

    @GetMapping("/types")
    public Optional<ApiResponse<List<ProductType>>> getProductTypes() {
        List<ProductType> productTypes = productService.findAllProductTypes();
        return Optional.of(productTypes)
                .map(ApiResponse::success);
    }

    @GetMapping("/categories")
    public Optional<ApiResponse<List<ProductCategory>>> getProductCategory() {
        List<ProductCategory> productCategories = productService.findAllProductCategories();
        return Optional.of(productCategories)
                .map(ApiResponse::success);
    }

    @GetMapping("/units")
    public Optional<ApiResponse<List<ProductUnit>>> getProductUnits() {
        List<ProductUnit> productUnits = productService.findAllProductUnits();
        return Optional.of(productUnits)
                .map(ApiResponse::success);
    }

    @GetMapping("/vat-types")
    public Optional<ApiResponse<List<ProductVatType>>> getProductVatType() {
        List<ProductVatType> productVatTypes = productService.findAllProductVatTypes();
        return Optional.of(productVatTypes)
                .map(ApiResponse::success);
    }

    @GetMapping("/inventory-types")
    public Optional<ApiResponse<List<ProductInventoryType>>> getProductInventoryTypes() {
        List<ProductInventoryType> productInventoryTypes = productService.findAllProductInventoryTypes();
        return Optional.of(productInventoryTypes)
                .map(ApiResponse::success);
    }

}
