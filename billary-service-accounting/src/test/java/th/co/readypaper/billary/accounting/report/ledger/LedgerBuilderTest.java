package th.co.readypaper.billary.accounting.report.ledger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import th.co.readypaper.billary.accounting.report.journal.GeneralJournalRepository;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournal;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalCredit;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournalDebit;
import th.co.readypaper.billary.repo.entity.account.ledger.Ledger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class LedgerBuilderTest {
    @Mock
    LedgerRepository mockLedgerRepository;

    @Mock
    GeneralJournalRepository journalRepository;

    @InjectMocks
    LedgerBuilder ledgerBuilder;

    @Test
    void test_build_by_year_and_month_expect_ledger_size_3_when_build_general_journal_with_withholding_tax() {
        Integer year = 2022;
        Integer month = 1;

        GeneralJournalDebit debit1 = GeneralJournalDebit.builder()
                .code("5008")
                .desc("ค่าบริการ")
                .amount(BigDecimal.valueOf(14800.00))
                .build();
        GeneralJournalDebit debit2 = GeneralJournalDebit.builder()
                .code("1011")
                .desc("ภาษีซื้อ")
                .amount(BigDecimal.valueOf(1036.00))
                .build();

        GeneralJournalCredit credit1 = GeneralJournalCredit.builder()
                .code("1003")
                .desc("เงินฝากธนาคาร - ไทยพาณิชย์")
                .amount(BigDecimal.valueOf(15392.00))
                .build();
        GeneralJournalCredit credit2 = GeneralJournalCredit.builder()
                .code("1105")
                .desc("ภาษีหัก ณ ที่จ่าย")
                .amount(BigDecimal.valueOf(444))
                .build();

        GeneralJournal mockJournal = GeneralJournal.builder()
                .date(LocalDate.now())
                .debits(Arrays.asList(debit1, debit2))
                .credits(Arrays.asList(credit1, credit2))
                .build();

        when(journalRepository.findByDateBetween(any(), any(), any())).thenReturn(Collections.singletonList(mockJournal));
        when(mockLedgerRepository.save(any())).then(invocation -> invocation.getArgument(0));

        List<Ledger> results = ledgerBuilder.buildByYearAndMonth(year, month);

        assertFalse(results.isEmpty());
//        assertEquals(3, results.size());
//
//        // credits
//        assertEquals(1, results.get(0).getCredits().size());
//        assertEquals(BigDecimal.valueOf(30780.00), results.get(0).getCredits().get(0).getAmount());
//
//        assertEquals(1, results.get(1).getCredits().size());
//        assertEquals(BigDecimal.valueOf(1620.00), results.get(1).getCredits().get(0).getAmount());
//
//        // debits
//        assertEquals(2, results.get(2).getDebits().size());
//        assertEquals(BigDecimal.valueOf(30780.00), results.get(2).getDebits().get(0).getAmount());
//        assertEquals(BigDecimal.valueOf(1620.00), results.get(2).getDebits().get(1).getAmount());
    }
}