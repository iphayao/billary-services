package th.co.readypaper.billary.ompanies.document;

import org.springframework.stereotype.Service;
import th.co.readypaper.billary.repo.entity.document.Document;
import th.co.readypaper.billary.repo.repository.DocumentRepository;

import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<Document> findAllDocuments() {
        return documentRepository.findAll();
    }

}
