package th.co.readypaper.billary.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ApiStatus {
    SUCCESS("Success"),
    FAILURE("Failure");

    @Getter
    private final String val;
}
