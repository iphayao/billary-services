package th.co.readypaper.billary.accounting.journal.model.dto;

import lombok.Data;
import th.co.readypaper.billary.common.model.dto.contact.ContactDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class JournalEntryDto {
    private String id;
    private String documentId;
    private String reference;
    private ContactDto contact;
    private LocalDate issuedDate;
    private int lineVatTypeId;
    private JournalEntryStatusDto status;
    private List<JournalEntryLineItemDto> lineItems;
    private boolean vatInclusive;
    private boolean useInputTax;
    private BigDecimal subTotal;
    private BigDecimal totalDiscount;
    private BigDecimal totalAfterDiscount;
    private BigDecimal exemptVatAmount;
    private BigDecimal vatableAmount;
    private BigDecimal vatAmount;
    private BigDecimal total;
    private String remark;
}
