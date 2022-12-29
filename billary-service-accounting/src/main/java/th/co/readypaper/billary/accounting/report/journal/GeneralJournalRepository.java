package th.co.readypaper.billary.accounting.report.journal;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GeneralJournalRepository extends JpaRepository<GeneralJournal, UUID> {
    List<GeneralJournal> findByDateBetween(LocalDate firstDay, LocalDate lastDay);
    List<GeneralJournal> findByDateBetween(LocalDate firstDay, LocalDate lastDay, Sort sort);
    Optional<Long> deleteByDateBetween(LocalDate firstDay, LocalDate lastDay);
    Optional<Long> countByDateBetween(LocalDate firstDay, LocalDate lastDay);

}
