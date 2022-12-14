package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.document.Document;

import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {

    Optional<Document> findDocumentByCompanyIdAndDocumentTypeId(UUID documentId, int documentTypeId);

    Optional<Document> findByCompanyIdAndDocumentTypeId(UUID documentId, int documentTypId);

}
