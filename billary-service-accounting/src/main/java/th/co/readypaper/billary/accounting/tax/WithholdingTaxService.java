package th.co.readypaper.billary.accounting.tax;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.accounting.tax.model.TaxType;
import th.co.readypaper.billary.accounting.tax.model.WithholdingTax;
import th.co.readypaper.billary.accounting.tax.model.WithholdingTaxReport;
import th.co.readypaper.billary.repo.entity.expense.Expense;
import th.co.readypaper.billary.repo.repository.CompanyRepository;
import th.co.readypaper.billary.repo.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static th.co.readypaper.billary.common.utils.DateUtils.*;

@Slf4j
@Service
public class WithholdingTaxService {
    private final CompanyRepository companyRepository;
    private final ExpenseRepository expenseRepository;
    private final WithholdingTaxMapper withholdingTaxMapper;

    public WithholdingTaxService(CompanyRepository companyRepository,
                                 ExpenseRepository expenseRepository,
                                 WithholdingTaxMapper withholdingTaxMapper) {
        this.companyRepository = companyRepository;
        this.expenseRepository = expenseRepository;
        this.withholdingTaxMapper = withholdingTaxMapper;
    }

    public Optional<WithholdingTaxReport> findWithholdingTax(Integer year, Integer month) {
        log.info("Get Input Tax for Year: {}, Month: {}", year, month);
        LocalDate firstDay = firstDayOf(year, month);
        LocalDate lastDay = lastDayOf(year, month);

        log.info("First day : {}", firstDay);
        log.info("Last day  : {}", lastDay);

        List<WithholdingTax> withholdingTaxes = expenseRepository.findByIssuedDateBetween(firstDay, lastDay).stream()
                .filter(expense -> {
                    if (expense.getWithholdingTaxAmount() != null) {
                        return expense.getWithholdingTaxAmount().compareTo(BigDecimal.ZERO) > 0;
                    }
                    return false;
                })
                .sorted(Comparator.comparing(Expense::getDocumentId))
                .peek(expense -> log.info("Expense ID: {}/{}", expense.getDocumentId(), expense.getContact().getName()))
                .map(expense -> {
                    WithholdingTax wht = withholdingTaxMapper.toWithholdingTaxEntry(expense);
                    wht.setPaymentAmount(expense.getVatableAmount().add(expense.getExemptVatAmount()));
                    return wht;
                })
                .map(entry -> {
                    if (isCompany(entry.getContactName())) {
                        entry.setTaxType(TaxType.PND53);
                    } else {
                        entry.setTaxType(TaxType.PND3);
                    }
                    return entry;
                })
                .toList();


        return companyRepository.findByCode("T000000001")
                .map(company -> WithholdingTaxReport.builder()
                        .taxYear(String.valueOf(year))
                        .taxMonth(getFullMonthOf(month))
                        .taxName(company.getName())
                        .taxId(company.getTaxId())
                        .taxAddress(company.getAddress())
                        .taxOffice(company.getOffice())
                        .taxOrganisation(company.getName())
                        .entries(withholdingTaxes)
                        .totalPaymentAmount(sumPaymentAmountOf(withholdingTaxes))
                        .totalPaidAmount(sumPaidAmount(withholdingTaxes))
                        .totalWithholdingTaxAmount(sumWithholdingTaxAmount(withholdingTaxes))
                        .build());
    }

    private BigDecimal sumWithholdingTaxAmount(List<WithholdingTax> withholdingTaxes) {
        return withholdingTaxes.stream()
                .map(WithholdingTax::getWithholdingTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal sumPaidAmount(List<WithholdingTax> withholdingTaxes) {
        return withholdingTaxes.stream()
                .map(WithholdingTax::getPaidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal sumPaymentAmountOf(List<WithholdingTax> withholdingTaxes) {
        return withholdingTaxes.stream()
                .map(WithholdingTax::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private boolean isCompany(String contactName) {
        return contactName.contains("บริษัท");
    }
}
