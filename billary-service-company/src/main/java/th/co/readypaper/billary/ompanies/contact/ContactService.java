package th.co.readypaper.billary.ompanies.contact;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.common.service.CompanyBaseService;
import th.co.readypaper.billary.ompanies.contact.model.ContactDto;
import th.co.readypaper.billary.repo.entity.contact.Contact;
import th.co.readypaper.billary.repo.repository.CompanyRepository;
import th.co.readypaper.billary.repo.repository.ContactRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContactService extends CompanyBaseService {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    public ContactService(CompanyRepository companyRepository,
                          ContactRepository contactRepository,
                          ContactMapper contactMapper) {
        super(companyRepository);
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    public List<ContactDto> getAllContact() {
        return contactRepository.findByCompanyId(getCompanyId()).stream()
                .map(contactMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ContactDto> getContactById(UUID id) {
        return contactRepository.findById(id)
                .map(contactMapper::toDto);
    }

    public Optional<ContactDto> findContactByName(String name) {
        return contactRepository.findFirstByName(name)
                .map(contactMapper::toDto);
    }

    public Optional<ContactDto> createNewContact(ContactDto newContact) {
        log.info("Create new Contact: {}", newContact);
        Contact contactEntity = contactMapper.toEntity(newContact);
        return Optional.of(contactRepository.save(contactEntity))
                .map(contactMapper::toDto);
    }

    public Optional<ContactDto> updateContactById(UUID id, ContactDto updateContact) {
        log.info("Update Contact ID: {}, name: {}", id, updateContact.getName());
        Contact updateContactEntity = contactMapper.toEntity(updateContact);
        return contactRepository.findById(id)
                .map(contact -> {
                    Contact mappedContact = contactMapper.update(contact, updateContactEntity);
                    return contactRepository.save(mappedContact);
                })
                .map(contactMapper::toDto);
    }

}
