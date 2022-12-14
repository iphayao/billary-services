package th.co.readypaper.billary.purchases.expense;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.analysis.function.Exp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.common.model.dto.expense.ExpenseVatTypeDto;
import th.co.readypaper.billary.common.model.dto.expense.WithholdingTaxPercentDto;
import th.co.readypaper.billary.common.service.DocumentIdBaseService;
import th.co.readypaper.billary.common.model.dto.expense.ExpenseDto;
import th.co.readypaper.billary.repo.entity.expense.Expense;
import th.co.readypaper.billary.repo.entity.expense.ExpenseStatus;
import th.co.readypaper.billary.repo.repository.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

@Slf4j
@Service
public class ExpenseService extends DocumentIdBaseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseVatTypeRepository expenseVatTypeRepository;
    private final WithholdingTaxPercentRepository withholdingTaxPercentRepository;
    private final ExpenseMapper expenseMapper;

    public ExpenseService(ExpenseRepository expenseRepository,
                          CompanyRepository companyRepository,
                          DocumentRepository documentRepository,
                          DocumentSerialRepository documentSerialRepository,
                          ExpenseVatTypeRepository expenseVatTypeRepository,
                          WithholdingTaxPercentRepository withholdingTaxPercentRepository,
                          ExpenseMapper expenseMapper) {
        super(companyRepository, documentRepository, documentSerialRepository, 7);
        this.expenseRepository = expenseRepository;
        this.expenseVatTypeRepository = expenseVatTypeRepository;
        this.withholdingTaxPercentRepository = withholdingTaxPercentRepository;
        this.expenseMapper = expenseMapper;
    }

    private Specification<Expense> hasContactName(String contact) {
        return (expense, cq, cb) -> cb.like(expense.get("contact").get("name"), "%" + contact + "%");
    }

    private Specification<Expense> hasDocumentId(String documentId) {
        return (expense, cq, cb) -> cb.like(expense.get("documentId"), "%" + documentId + "%");
    }

    public ResultPage<ExpenseDto> findAllExpenses(Integer page, Integer limit, Map<String, Object> params) {
        log.info("Find all expense, page: {}, limit: {}, params: {}", page, limit, params);
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "documentId"));

        Page<Expense> expensePage = expenseRepository.findAll(
                where(hasDocumentId((String) params.get("documentId")))
                        .and(hasContactName((String) params.get("contact"))),
                pageable);
        List<ExpenseDto> expenses = expensePage.map(expenseMapper::toDto).toList();

        return ResultPage.of(expenses, page, limit, (int) expensePage.getTotalElements());
    }

    public Optional<ExpenseDto> findExpenseById(UUID id) {
        log.info("Find expense by ID: {}", id);
        return expenseRepository.findById(id)
                .map(expenseMapper::toDto);
    }

    public Optional<ExpenseDto> createNewExpense(ExpenseDto newExpenseDto) {
        log.info("Create new expense document ID: {}", newExpenseDto.getDocumentId());
        Expense newExpenseEntity = expenseMapper.toEntity(newExpenseDto);
        if (newExpenseEntity.getStatus().equals(ExpenseStatus.DRAFT)) {
            newExpenseEntity.setStatus(ExpenseStatus.SUBMITTED);
        }

        if (newExpenseEntity.getPayment() != null) {
            if (newExpenseEntity.getPayment().getExpense() == null) {
                newExpenseEntity.getPayment().setExpense(newExpenseEntity);
            }
            newExpenseEntity.setStatus(ExpenseStatus.PAID);
        } else if (newExpenseEntity.getPayment() == null) {
            newExpenseEntity.setStatus(ExpenseStatus.SUBMITTED);
        }

        newExpenseEntity.setLineItems(newExpenseEntity.getLineItems().stream()
                .map(expenseLineItem -> {
                    expenseLineItem.setExpense(newExpenseEntity);
                    return expenseLineItem;
                }).collect(Collectors.toList()));

        Expense savedExpenseEntity = expenseRepository.save(newExpenseEntity);
        updateDocumentId(savedExpenseEntity.getDocumentId());

        assert savedExpenseEntity.getId() != null;
        return expenseRepository.findById(savedExpenseEntity.getId())
                .map(expenseMapper::toDto);
    }

    public Optional<ExpenseDto> updateExpenseById(UUID id, ExpenseDto updateExpenseDto) {
        log.info("Update expense by ID: {}/{}", updateExpenseDto.getDocumentId(), id);
        Expense updateExpenseEntity = expenseMapper.toEntity(updateExpenseDto);
        return expenseRepository.findById(id)
                .map(expense -> {
                    Expense mappedExpense = expenseMapper.update(expense, updateExpenseEntity);
                    if (mappedExpense.getPayment() != null) {
                        if (mappedExpense.getPayment().getExpense() == null) {
                            mappedExpense.getPayment().setExpense(mappedExpense);
                        }
                        mappedExpense.setStatus(ExpenseStatus.PAID);
                    } else if (mappedExpense.getPayment() == null) {
                        mappedExpense.setStatus(ExpenseStatus.SUBMITTED);
                    }
                    mappedExpense.setLineItems(mappedExpense.getLineItems().stream()
                            .map(expenseLineItem -> {
                                expenseLineItem.setExpense(expense);
                                return expenseLineItem;
                            }).collect(Collectors.toList()));
                    return expenseRepository.save(mappedExpense);
                })
                .map(expenseMapper::toDto);
    }

    public List<ExpenseVatTypeDto> findAllProductVatType() {
        log.info("Find all product VAT types");
        return expenseVatTypeRepository.findAll().stream()
                .map(expenseMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<WithholdingTaxPercentDto> findAllWithholdingTaxPercent() {
        log.info("Find all withholding TAX percents");
        return withholdingTaxPercentRepository.findAll().stream()
                .map(expenseMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteExpenseById(UUID id) {
        log.info("Delete expense by ID: {}", id);
        expenseRepository.findById(id)
                .ifPresent(expenseRepository::delete);
    }
}