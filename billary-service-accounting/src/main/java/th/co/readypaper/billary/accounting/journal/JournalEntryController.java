package th.co.readypaper.billary.accounting.journal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.accounting.journal.model.dto.JournalEntryDto;
import th.co.readypaper.billary.common.model.ApiResponse;
import th.co.readypaper.billary.common.model.DocumentId;
import th.co.readypaper.billary.common.model.ResultPage;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/journal-entries")
public class JournalEntryController {
    private final JournalEntryService journalEntryService;

    public JournalEntryController(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    @GetMapping("/document-id")
    public Optional<ApiResponse<DocumentId>> getDocumentId(@RequestParam(value = "publishedOn", required = false)
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishedOn) {
        return journalEntryService.generateDocumentId(publishedOn)
                .map(ApiResponse::success);
    }

    @GetMapping
    public Optional<ApiResponse<ResultPage<JournalEntryDto>>> getJournalEntries(@RequestParam Integer page,
                                                                                @RequestParam Integer limit,
                                                                                @RequestParam Map<String, Object> params) {
        var journalEntries = journalEntryService.findJournals(page, limit, params);
        return Optional.of(journalEntries)
                .map(ApiResponse::success);
    }

    @GetMapping("/{id}")
    public Optional<ApiResponse<JournalEntryDto>> getJournalEntry(@PathVariable UUID id) {
        return journalEntryService.findJournalEntryById(id)
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<JournalEntryDto>> postJournalEntry(@RequestBody JournalEntryDto body) {
        return journalEntryService.createJournalEntry(body)
                .map(ApiResponse::success);
    }

    @PutMapping("/{id}")
    public Optional<ApiResponse<JournalEntryDto>> putJournalEntry(@PathVariable UUID id, @RequestBody JournalEntryDto body) {
        return journalEntryService.updateJournalEntryById(id, body)
                .map(ApiResponse::success);
    }

}
