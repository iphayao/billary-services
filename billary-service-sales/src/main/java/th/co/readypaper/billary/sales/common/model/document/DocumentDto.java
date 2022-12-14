package th.co.readypaper.billary.sales.common.model.document;

import lombok.Data;
import th.co.readypaper.billary.sales.common.model.dto.ContactDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class DocumentDto {
    private String id;
    private String documentId;
    private String reference;
    private ContactDto contact;
    private LocalDate issuedDate;
    private LocalDate dueDate;
    private int lineVatTypeId;
    private String saleName;
    private String saleChannel;
    private boolean sentToContact;
    private LocalDate expectPaymentDate;
    private LocalDate plannedPaymentDate;
    private List<DocumentLineItemDto> lineItems;
    private BigDecimal subTotal;
    private BigDecimal totalDiscount;
    private BigDecimal totalAfterDiscount;
    private BigDecimal exemptVatAmount;
    private BigDecimal vatableAmount;
    private BigDecimal vatAmount;
    private BigDecimal total;
    private String remark;
}
