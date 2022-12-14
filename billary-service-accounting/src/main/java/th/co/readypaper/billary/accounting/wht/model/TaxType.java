package th.co.readypaper.billary.accounting.wht.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaxType {
    PND3("ภ.ง.ด. 3"),
    PND53("ภ.ง.ด. 53");

    private final String val;

    TaxType(String val) {
        this.val = val;
    }

    @JsonValue
    public String getVal() {
        return val;
    }

}
