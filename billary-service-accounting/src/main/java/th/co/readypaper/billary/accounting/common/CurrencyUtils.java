package th.co.readypaper.billary.accounting.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyUtils {
    public static int bahtOf(BigDecimal amount) {
        return amount.toBigInteger().intValue();
    }

    public static int stangOf(BigDecimal amount) {
        return amount.remainder(BigDecimal.ONE).setScale(2, RoundingMode.HALF_DOWN)
                .multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_DOWN).intValue();
    }
}
