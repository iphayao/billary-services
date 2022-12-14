package th.co.readypaper.billary.sales.common.model.document;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DocumentType {
    SHORT("short"), FULL("full");

    @Getter
    private String val;
}
