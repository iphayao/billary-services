package th.co.readypaper.billary.accounting.report.vat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.accounting.report.vat.model.ValueAddedTaxReport;
import th.co.readypaper.billary.accounting.report.vat.model.ValueAddedTax;
import th.co.readypaper.billary.accounting.report.vat.model.VatReportType;
import th.co.readypaper.billary.repo.entity.expense.Expense;
import th.co.readypaper.billary.repo.repository.CompanyRepository;
import th.co.readypaper.billary.repo.repository.ExpenseRepository;
import th.co.readypaper.billary.repo.repository.InvoiceRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static th.co.readypaper.billary.common.utils.DateUtils.*;

@Slf4j
@Service
public class ValueAddedTaxService {
    private final CompanyRepository companyRepository;
    private final InvoiceRepository invoiceRepository;
    private final ExpenseRepository expenseRepository;
    private final ValueAddedTaxMapper valueAddedTaxMapper;
    private final ValueAddedTaxGenerator valueAddedTaxGenerator;

    public ValueAddedTaxService(CompanyRepository companyRepository,
                                InvoiceRepository invoiceRepository,
                                ExpenseRepository expenseRepository,
                                ValueAddedTaxMapper valueAddedTaxMapper,
                                ValueAddedTaxGenerator valueAddedTaxGenerator) {
        this.companyRepository = companyRepository;
        this.invoiceRepository = invoiceRepository;
        this.expenseRepository = expenseRepository;
        this.valueAddedTaxMapper = valueAddedTaxMapper;
        this.valueAddedTaxGenerator = valueAddedTaxGenerator;
    }

    public Optional<ValueAddedTaxReport> findOutputTax(Integer year, Integer month) {
        log.info("Get Output Tax for Year: {}, Month: {}", year, month);
        LocalDate firstDay = firstDayOf(year, month);
        LocalDate lastDay = lastDayOf(year, month);

        LocalDateTime firstDayDateTime = convertToUTC(startDayOf(firstDay));
        LocalDateTime lastDayDateTime = convertToUTC(endDayOf(lastDay));

        log.info("First day (DateTime): {}", firstDayDateTime);
        log.info("Last day (DateTime): {}", lastDayDateTime);

        List<ValueAddedTax> valueAddedTaxes = invoiceRepository.findByIssuedDateBetween(firstDay, lastDay).stream()
                .filter(invoice -> {
                    if (invoice.getVatAmount() != null) {
                        return invoice.getVatAmount().compareTo(BigDecimal.ZERO) > 0;
                    }
                    return false;
                })
                .peek(invoice -> log.info("Invoice ID: {}", invoice.getDocumentId()))
                .map(valueAddedTaxMapper::toValueAddedTaxEntry)
                .peek(valueAddedTax -> {
                    valueAddedTax.setVatableAmount(amountOf(valueAddedTax.getVatableAmount()));
                    valueAddedTax.setVatAmount(amountOf(valueAddedTax.getVatAmount()));
                    valueAddedTax.setTotal(amountOf(valueAddedTax.getTotal()));
                }).toList();

        return companyRepository.findByCode("T000000001")
                .map(company -> ValueAddedTaxReport.builder()
                        .type(VatReportType.OUTPUT_TAX)
                        .taxYear(String.valueOf(year))
                        .taxMonth(getFullMonthOf(month))
                        .taxName(company.getName())
                        .taxId(company.getTaxId())
                        .taxOffice(company.getOffice())
                        .taxAddress(company.getAddress())
                        .taxOrganisation(company.getName())
                        .entries(valueAddedTaxes)
                        .totalVatableAmount(sumVatableAmountOf(valueAddedTaxes))
                        .totalVatAmount(sumVatAmountOf(valueAddedTaxes))
                        .totalAmount(sumTotalAmountOf(valueAddedTaxes))
                        .build());
    }

    public Optional<ValueAddedTaxReport> findInputTax(Integer year, Integer month) {
        log.info("Get Input Tax for Year: {}, Month: {}", year, month);
        LocalDate firstDay = firstDayOf(year, month);
        LocalDate lastDay = lastDayOf(year, month);

        log.info("First day : {}", firstDay);
        log.info("Last day  : {}", lastDay);

        List<ValueAddedTax> valueAddedTaxes = expenseRepository.findByIssuedDateBetween(firstDay, lastDay).stream()
                .filter(expense -> {
                    if (expense.getVatAmount() != null) {
                        return expense.getVatAmount().compareTo(BigDecimal.ZERO) > 0;
                    }
                    return false;
                })
                .sorted(Comparator.comparing(Expense::getDocumentId))
                .peek(expense -> log.info("Expense ID: {}/{}", expense.getDocumentId(), expense.getContact().getName()))
                .map(valueAddedTaxMapper::toValueAddedTaxEntry)
                .peek(valueAddedTax -> {
                    valueAddedTax.setVatableAmount(amountOf(valueAddedTax.getVatableAmount()));
                    valueAddedTax.setVatAmount(amountOf(valueAddedTax.getVatAmount()));
                    valueAddedTax.setTotal(amountOf(valueAddedTax.getTotal()));
                }).toList();

        return companyRepository.findByCode("T000000001")
                .map(company -> ValueAddedTaxReport.builder()
                        .type(VatReportType.INPUT_TAX)
                        .taxYear(String.valueOf(year))
                        .taxMonth(getFullMonthOf(month))
                        .taxName(company.getName())
                        .taxId(company.getTaxId())
                        .taxOffice(company.getOffice())
                        .taxAddress(company.getAddress())
                        .taxOrganisation(company.getName())
                        .entries(valueAddedTaxes)
                        .totalVatableAmount(sumVatableAmountOf(valueAddedTaxes))
                        .totalVatAmount(sumVatAmountOf(valueAddedTaxes))
                        .totalAmount(sumTotalAmountOf(valueAddedTaxes))
                        .build());
    }

    public Optional<byte[]> generateOutputTax(Integer year, Integer month) {
        log.info("Generate Output Tax for Year: {}, Month: {}", year, month);
        return findOutputTax(year, month)
                .map(valueAddedTaxGenerator::generate);
    }

    public Optional<byte[]> generateInputTax(Integer year, Integer month) {
        log.info("Generate Input Tax for Year: {}, Month: {}", year, month);
        return findInputTax(year, month)
                .map(valueAddedTaxGenerator::generate);
    }

    private boolean isInputTax(Expense expense) {
        //return expense.getIsInputTax() != null && expense.getIsInputTax();
        if(expense.isUseInputTax()) {
            return true; // expense.getIsInputTax();
        }
        return false;
    }

    private BigDecimal sumVatableAmountOf(List<ValueAddedTax> vatEntries) {
        return vatEntries.stream()
                .map(ValueAddedTax::getVatableAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal sumVatAmountOf(List<ValueAddedTax> vatEntries) {
        return vatEntries.stream()
                .map(ValueAddedTax::getVatAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal sumTotalAmountOf(List<ValueAddedTax> vatEntries) {
        return vatEntries.stream()
                .map(ValueAddedTax::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal amountOf(BigDecimal amount) {
        return (amount != null) ? amount.setScale(2, RoundingMode.HALF_UP) : BigDecimal.valueOf(0.00);
    }
}
