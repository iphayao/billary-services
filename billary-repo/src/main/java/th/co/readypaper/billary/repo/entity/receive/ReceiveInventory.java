package th.co.readypaper.billary.repo.entity.receive;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;
import th.co.readypaper.billary.repo.entity.contact.Contact;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class ReceiveInventory extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;
    private String documentId;
    private String reference;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    private Contact contact;
    private LocalDate issuedDate;
    private LocalDate deliveryDate;
    private int lineVatTypeId;
    private int statusId;
    private boolean sentToContact;
    private String attentionTo;
    private String deliveryAddress;
    private LocalDate expectArriveDate;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "receive_inventory_id")
    private List<ReceiveInventoryLineItem> lineItems;
    private BigDecimal subTotal;
    private BigDecimal totalDiscount;
    private BigDecimal totalAfterDiscount;
    private BigDecimal exemptVatAmount;
    private BigDecimal vatableAmount;
    private BigDecimal vatAmount;
    private BigDecimal total;
}
