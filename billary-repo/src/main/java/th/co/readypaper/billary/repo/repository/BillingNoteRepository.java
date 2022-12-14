package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.billing.BillingNote;

import java.util.Optional;
import java.util.UUID;

public interface BillingNoteRepository extends JpaRepository<BillingNote, UUID> {
    Optional<BillingNote> findByDocumentId(String documentId);
}
