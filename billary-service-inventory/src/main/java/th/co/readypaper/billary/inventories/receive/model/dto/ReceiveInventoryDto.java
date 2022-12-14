package th.co.readypaper.billary.inventories.receive.model.dto;

import lombok.Data;
import th.co.readypaper.billary.inventories.common.model.dto.ContactDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ReceiveInventoryDto {
    private String id;
    private String documentId;
    private String reference;
    private ContactDto contact;
    private LocalDate issuedDate;
    private LocalDate deliveryDate;
    private int lineVatTypeId;
    private int statusId;
    private boolean sentToContact;
    private String attentionTo;
    private String deliveryAddress;
    private LocalDate expectArriveDate;
    private List<ReceiveInventoryLineItemDto> lineItems;
    private BigDecimal subTotal;
    private BigDecimal totalDiscount;
    private BigDecimal totalAfterDiscount;
    private BigDecimal exemptVatAmount;
    private BigDecimal vatableAmount;
    private BigDecimal vatAmount;
    private BigDecimal total;
}
