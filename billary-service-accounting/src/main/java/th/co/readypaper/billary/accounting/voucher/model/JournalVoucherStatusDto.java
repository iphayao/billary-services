package th.co.readypaper.billary.accounting.voucher.model;

import lombok.Data;

@Data
public class JournalVoucherStatusDto {
    private Integer id;
    private String name;
    private String code;
    private String description;
}
