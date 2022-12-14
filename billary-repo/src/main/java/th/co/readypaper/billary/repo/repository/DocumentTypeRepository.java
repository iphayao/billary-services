package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.document.DocumentType;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer> {
}
