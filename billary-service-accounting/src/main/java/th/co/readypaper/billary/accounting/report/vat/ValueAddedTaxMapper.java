package th.co.readypaper.billary.accounting.report.vat;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import th.co.readypaper.billary.accounting.report.vat.model.ValueAddedTax;
import th.co.readypaper.billary.repo.entity.expense.Expense;
import th.co.readypaper.billary.repo.entity.invoice.Invoice;

@Mapper(componentModel = "spring")
public interface ValueAddedTaxMapper {

    @Mapping(target = "contactName", source = "contact.name")
    @Mapping(target = "contactTaxId", source = "contact.taxId")
    @Mapping(target = "contactOffice", source = "contact.office")
    ValueAddedTax toValueAddedTaxEntry(Invoice invoice);

    @Mapping(target = "contactName", source = "contact.name")
    @Mapping(target = "contactTaxId", source = "contact.taxId")
    @Mapping(target = "contactOffice", source = "contact.office")
    ValueAddedTax toValueAddedTaxEntry(Expense expense);

//    @Mapping(target = "contactName", source = "contact.name")
//    @Mapping(target = "contactTaxId", source = "contact.taxId")
//    @Mapping(target = "contactOffice", source = "contact.office")
//    @Mapping(target = "vatAmount", source = "total")
//    ValueAddedTax toValueAddedTaxEntry(JournalEntry journalEntry);

}
