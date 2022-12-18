package th.co.readypaper.billary.ompanies.contact;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.common.model.dto.contact.BusinessTypeDto;
import th.co.readypaper.billary.common.model.dto.contact.ContactDto;
import th.co.readypaper.billary.common.model.dto.contact.ContactTypeDto;
import th.co.readypaper.billary.common.service.CompanyBaseService;
import th.co.readypaper.billary.repo.entity.contact.Contact;
import th.co.readypaper.billary.repo.repository.BusinessTypeRepository;
import th.co.readypaper.billary.repo.repository.CompanyRepository;
import th.co.readypaper.billary.repo.repository.ContactRepository;
import th.co.readypaper.billary.repo.repository.ContactTypeRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContactService extends CompanyBaseService {
    private final ContactRepository contactRepository;
    private final ContactTypeRepository contactTypeRepository;
    private final BusinessTypeRepository businessTypeRepository;
    private final ContactMapper contactMapper;

    public ContactService(CompanyRepository companyRepository,
                          ContactRepository contactRepository,
                          ContactTypeRepository contactTypeRepository,
                          BusinessTypeRepository businessTypeRepository,
                          ContactMapper contactMapper) {
        super(companyRepository);
        this.contactRepository = contactRepository;
        this.contactTypeRepository = contactTypeRepository;
        this.businessTypeRepository = businessTypeRepository;
        this.contactMapper = contactMapper;
    }

    public List<ContactDto> getAllContact() {
        return contactRepository.findByCompanyId(getCompanyId()).stream()
                .map(contactMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResultPage<ContactDto> findAllContacts(Integer page, Integer limit, Map<String, Object> params) {
        log.info("Find all contact, page: {}, limit: {}, params: {}", page, limit, params);
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "name"));

        Page<Contact> contactPage = contactRepository.findAll(pageable);
        List<ContactDto> contacts = contactPage.map(contactMapper::toDto).toList();

        return ResultPage.of(contacts, page, limit, (int) contactPage.getTotalElements());
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

    public void deleteContactById(UUID id) {
        log.info("Delete Contact ID: {}", id);
        contactRepository.findById(id)
                .ifPresent(contactRepository::delete);
    }

    public List<ContactTypeDto> findContactTypes() {
        return contactTypeRepository.findAll().stream()
                .map(contactMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BusinessTypeDto> findBusinessTypes() {
        return businessTypeRepository.findAll().stream()
                .map(contactMapper::toDto)
                .collect(Collectors.toList());
    }
}
