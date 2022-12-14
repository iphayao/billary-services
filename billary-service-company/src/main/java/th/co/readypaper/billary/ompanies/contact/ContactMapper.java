package th.co.readypaper.billary.ompanies.contact;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import th.co.readypaper.billary.ompanies.contact.model.ContactDto;
import th.co.readypaper.billary.repo.entity.contact.Contact;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    ContactDto toDto(Contact contact);

    @Mapping(target = "id", ignore = true)
    Contact toEntity(ContactDto dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    Contact update(@MappingTarget Contact contact, Contact updateContactEntity);
}
