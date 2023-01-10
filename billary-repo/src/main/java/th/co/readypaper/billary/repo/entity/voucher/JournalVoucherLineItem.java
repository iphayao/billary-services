package th.co.readypaper.billary.repo.entity.voucher;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;
import th.co.readypaper.billary.repo.entity.account.chart.AccountChart;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class JournalVoucherLineItem extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;
    private int itemOrder;
    @ManyToOne
    @JoinColumn(name = "journal_voucher_id",
            referencedColumnName = "id")
    private JournalVoucher journalVoucher;
    @ManyToOne
    @JoinColumn(name = "account_chart_id")
    private AccountChart accountChart;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
}
