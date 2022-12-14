package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.product.ProductVatType;

public interface ProductVatTypeRepository extends JpaRepository<ProductVatType, Integer> {
}
