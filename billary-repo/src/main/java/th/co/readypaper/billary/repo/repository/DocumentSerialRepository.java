package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.document.DocumentSerial;

import java.util.Optional;
import java.util.UUID;

public interface DocumentSerialRepository extends JpaRepository<DocumentSerial, UUID> {

    Optional<DocumentSerial> findByDocumentId(UUID documentId);

    Optional<DocumentSerial> findByDocumentIdAndCurrentMonthAndCurrentYear(UUID id, int currentMonth, int currentYear);
}
