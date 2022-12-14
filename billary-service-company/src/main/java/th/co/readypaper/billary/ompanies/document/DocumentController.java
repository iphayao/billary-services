package th.co.readypaper.billary.ompanies.document;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.readypaper.billary.common.model.ApiResponse;
import th.co.readypaper.billary.repo.entity.document.Document;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public Optional<ApiResponse<List<Document>>> getDocuments() {
        List<Document> documents = documentService.findAllDocuments();
        return Optional.of(documents)
                .map(ApiResponse::success);
    }

}
