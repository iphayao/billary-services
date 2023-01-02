package th.co.readypaper.billary.accounting.journal.model.dto;

import lombok.Data;

@Data
public class JournalEntryStatusDto {
    private Integer id;
    private String name;
    private String code;
    private String description;
}
