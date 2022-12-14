package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.product.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {
}
