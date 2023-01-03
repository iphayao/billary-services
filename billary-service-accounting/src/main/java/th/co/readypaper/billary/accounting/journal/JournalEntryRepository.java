package th.co.readypaper.billary.accounting.journal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import th.co.readypaper.billary.repo.entity.journal.JournalEntry;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, UUID>, JpaSpecificationExecutor<JournalEntry> {
    List<JournalEntry> findByIssuedDateBetween(LocalDate startDate, LocalDate endDate);
}
