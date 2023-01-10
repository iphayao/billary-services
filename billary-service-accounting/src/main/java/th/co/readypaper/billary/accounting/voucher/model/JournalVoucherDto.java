package th.co.readypaper.billary.accounting.voucher.model;

import lombok.Data;
import th.co.readypaper.billary.common.model.dto.contact.ContactDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class JournalVoucherDto {
    private String id;
    private String documentId;
    private String reference;
    private ContactDto contact;
    private LocalDate issuedDate;
    private String description;
    private JournalVoucherStatusDto status;
    private List<JournalVoucherLineItemDto> lineItems;
    private BigDecimal totalDebitAmount;
    private BigDecimal totalCreditAmount;
    private String remark;
}
