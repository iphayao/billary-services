package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.invoice.Invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    List<Invoice> findByOrderByDocumentId();
    Optional<Invoice> findByDocumentId(String documentId);

    List<Invoice> findByIssuedDateBetween(LocalDate startDate, LocalDate endDate);

}
