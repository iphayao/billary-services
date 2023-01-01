package th.co.readypaper.billary.purchases.expense;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import th.co.readypaper.billary.common.model.LineItemMapper;
import th.co.readypaper.billary.common.model.dto.expense.ExpenseDto;
import th.co.readypaper.billary.common.model.dto.expense.ExpensePaymentTypeDto;
import th.co.readypaper.billary.common.model.dto.expense.ExpenseVatTypeDto;
import th.co.readypaper.billary.common.model.dto.expense.WithholdingTaxPercentDto;
import th.co.readypaper.billary.repo.entity.expense.*;

@Mapper(componentModel = "spring")
public interface ExpenseMapper extends LineItemMapper<ExpenseLineItem> {
    ExpenseDto toDto(Expense entity);
    ExpenseVatTypeDto toDto(ExpenseVatType entity);
    WithholdingTaxPercentDto toDto(WithholdingTaxPercent entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    Expense toEntity(ExpenseDto dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "lineItems", qualifiedByName = "lineItems")
    })
    Expense update(@MappingTarget Expense target, Expense source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    ExpenseLineItem update(@MappingTarget ExpenseLineItem target, ExpenseLineItem source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "expense", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
    })
    ExpensePayment update(@MappingTarget ExpensePayment target, ExpensePayment source);

    ExpensePaymentTypeDto toDto(ExpensePaymentType expensePaymentType);
}
