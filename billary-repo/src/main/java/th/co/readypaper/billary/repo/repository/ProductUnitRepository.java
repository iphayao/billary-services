package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.product.ProductUnit;

public interface ProductUnitRepository extends JpaRepository<ProductUnit, Integer> {
}
