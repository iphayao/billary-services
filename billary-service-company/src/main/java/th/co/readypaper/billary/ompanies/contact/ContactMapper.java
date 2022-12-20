package th.co.readypaper.billary.ompanies.contact;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import th.co.readypaper.billary.common.model.dto.contact.BusinessTypeDto;
import th.co.readypaper.billary.common.model.dto.contact.ContactDto;
import th.co.readypaper.billary.common.model.dto.contact.ContactTypeDto;
import th.co.readypaper.billary.repo.entity.contact.BusinessType;
import th.co.readypaper.billary.repo.entity.contact.Contact;
import th.co.readypaper.billary.repo.entity.contact.ContactType;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    ContactDto toDto(Contact contact);
    ContactTypeDto toDto(ContactType entity);
    BusinessTypeDto toDto(BusinessType entity);

    @Mapping(target = "id", ignore = true)
    Contact toEntity(ContactDto dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    Contact update(@MappingTarget Contact contact, Contact updateContactEntity);
}
