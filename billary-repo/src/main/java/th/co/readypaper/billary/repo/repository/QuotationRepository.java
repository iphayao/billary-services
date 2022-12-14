package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.quotation.Quotation;

import java.util.UUID;

public interface QuotationRepository extends JpaRepository<Quotation, UUID> {
}
