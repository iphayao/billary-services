package th.co.readypaper.billary.common.utils;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class FileNameUtils {

    private static final List<String> months = Arrays.asList("", "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม");

    public static HttpHeaders attachment(String title) {
        String fileName = title + ".xlsx";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename(URLEncoder.encode(fileName, StandardCharsets.UTF_8))
                .build());

        return headers;
    }

    public static HttpHeaders attachment(String title, Integer year, Integer month) {
        String fileName = title + "-" + (year + 543) + "-" + String.format("%02d", month) + "-" + months.get(month) + ".xlsx";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename(URLEncoder.encode(fileName, StandardCharsets.UTF_8))
                .build());

        return headers;
    }

}
