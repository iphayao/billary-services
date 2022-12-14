package th.co.readypaper.billary.ompanies.contact;

import org.springframework.web.bind.annotation.*;
import th.co.readypaper.billary.common.model.ApiResponse;
import th.co.readypaper.billary.ompanies.contact.model.ContactDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public Optional<ApiResponse<List<ContactDto>>> getContacts() {
        return Optional.of(contactService.getAllContact())
                .map(ApiResponse::success);
    }

    @GetMapping("/{id}")
    public Optional<ApiResponse<ContactDto>> getContact(@PathVariable UUID id) {
        return contactService.getContactById(id)
                .map(ApiResponse::success);
    }

    @GetMapping(params = {"name"})
    public Optional<ApiResponse<ContactDto>> getContact(@RequestParam String name) {
        return contactService.findContactByName(name)
                .map(ApiResponse::success);
    }

    @PostMapping
    public Optional<ApiResponse<ContactDto>> postContact(@RequestBody ContactDto newContact) {
        return contactService.createNewContact(newContact)
                .map(ApiResponse::success);
    }

    @PutMapping("/{id}")
    public Optional<ApiResponse<ContactDto>> putContact(@PathVariable UUID id, @RequestBody ContactDto updateContact) {
        return contactService.updateContactById(id, updateContact)
                .map(ApiResponse::success);
    }

}
