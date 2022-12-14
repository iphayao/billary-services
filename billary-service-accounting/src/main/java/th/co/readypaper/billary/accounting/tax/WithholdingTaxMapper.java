package th.co.readypaper.billary.accounting.tax;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import th.co.readypaper.billary.accounting.tax.model.WithholdingTax;
import th.co.readypaper.billary.repo.entity.expense.Expense;

@Mapper(componentModel = "spring")
public interface WithholdingTaxMapper {

    @Mapping(target = "contactName", source = "contact.name")
    @Mapping(target = "contactTaxId", source = "contact.taxId")
    @Mapping(target = "contactOffice", source = "contact.office")
    @Mapping(target = "withholdingTaxPercent", source = "withholdingTaxPercent.percent")
    @Mapping(target = "paymentDate", source = "payment.paymentDate")
    @Mapping(target = "paidAmount", source = "payment.paidAmount")
    WithholdingTax toWithholdingTaxEntry(Expense expense);

}
