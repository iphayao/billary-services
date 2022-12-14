package th.co.readypaper.billary.sales.common.model.document;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ThaiAmountWordingConvertor {
    private static final String BATH = "บาท";
    private static final String SATANG = "สตางค์";
    private static final String NET_AMOUNT = "ถ้วน";

    private static final List<String> num = Arrays.asList("ศูนย์", "หนึ่ง", "สอง", "สาม", "สี่", "ห้า", "หก", "เจ็ด", "แปด", "เก้า");
    private static final List<String> special = Arrays.asList("", "เอ็ด", "ยี่");
    private static final List<String> unit = Arrays.asList("", "สิบ", "ร้อย", "พัน", "หมื่น", "แสน", "ล้าน");

    private ThaiAmountWordingConvertor() {
    }

    public static String toText(BigDecimal amount) {
        StringBuilder stb = new StringBuilder();
        int scale = amount.scale();

        BigDecimal scaleAmount = null;
        BigDecimal millionAmount = null;
        if (scale > 0) {
            scaleAmount = amount.remainder(amount.setScale(0, RoundingMode.FLOOR))
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(0, RoundingMode.FLOOR);

            if (scaleAmount.equals(BigDecimal.ZERO)) {
                scaleAmount = null;
            }

            amount = amount.setScale(0, RoundingMode.FLOOR);
            if(amount.precision() > 6) {
                BigDecimal divisor = BigDecimal.valueOf(10).pow(6);
                millionAmount = amount.divide(divisor, RoundingMode.FLOOR);
                amount = amount.remainder(divisor);
            }
        }

        if(millionAmount != null) {
            stb.append(buildText(millionAmount));
            stb.append(unit.get(unit.size() - 1));
        }

        stb.append(buildText(amount));
        stb.append(BATH);

        if (scaleAmount != null) {
            stb.append(buildText(scaleAmount));
            stb.append(SATANG);
        } else {
            stb.append(NET_AMOUNT);
        }

        return stb.toString();
    }

    private static String buildText(BigDecimal amount) {
        StringBuilder stb = new StringBuilder();
        int precision = amount.precision() - 1;

        for (int i = precision; i >= 0; i--) {
            BigDecimal divisor = BigDecimal.valueOf(10).pow(i);
            BigDecimal val = amount.divide(divisor, RoundingMode.FLOOR);

            if (!val.equals(BigDecimal.ZERO)) {
                if (val.intValue() == 2 && i == 1) {
                    stb.append(special.get(val.intValue()));
                } else if (val.intValue() == 1 && i == 0) {
                    stb.append(special.get(val.intValue()));
                } else if (val.intValue() != 1 || i != 1) {
                    stb.append(num.get(val.intValue()));
                }
                stb.append(unit.get(i));
            }

            amount = amount.remainder(divisor);
        }

        return stb.toString();
    }
}
