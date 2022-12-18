package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.contact.ContactType;

public interface ContactTypeRepository extends JpaRepository<ContactType, Integer> {
}
