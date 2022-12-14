package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.contact.Contact;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, UUID> {

//    Optional<Contact> save(Contact contact);
    List<Contact> findByCompanyId(UUID companyId);
    Optional<Contact> findFirstByName(String name);

}
