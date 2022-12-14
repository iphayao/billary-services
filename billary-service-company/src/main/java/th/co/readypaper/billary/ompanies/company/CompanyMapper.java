package th.co.readypaper.billary.ompanies.company;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import th.co.readypaper.billary.repo.entity.company.Company;
import th.co.readypaper.billary.ompanies.company.model.CompanyDto;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyDto toDto(Company entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true)
    })
    Company toEntity(CompanyDto dto);

}
