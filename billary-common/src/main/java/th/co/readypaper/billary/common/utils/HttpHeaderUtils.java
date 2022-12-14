package th.co.readypaper.billary.common.utils;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HttpHeaderUtils {

    private static List<String> months = Arrays.asList("", "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม");

    public static HttpHeaders attachment(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename(id.toString() + ".pdf")
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

    public static HttpHeaders attachment(String docId, UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename(docId + "_" + id.toString() + ".pdf")
                .build());

        return headers;
    }

    public static HttpHeaders journalAttachmentFilename(int year, int month) {
        return attachmentFilename(year, month, "บัญชีรายวัน");
    }

    public static HttpHeaders ledgerAttachmentFilename(int year, int month) {
        return attachmentFilename(year, month, "บัญชีแยกประเภท");
    }

    public static HttpHeaders trialBalanceAttachmentFilename(int year, int month) {
        return attachmentFilename(year, month, "งบทดลอง");
    }

    public static HttpHeaders attachmentFilename(int year, int month, String prefix) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename(prefix + "-" + getYear(year) + "-" + getMonth(month) + ".xlsx", StandardCharsets.UTF_8)
                .build());
        return headers;
    }

    private static String getYear(int year) {
        return String.valueOf(year + 543);
    }

    private static String getMonth(int month) {
        return DateUtils.FULL_MONTHS[month];
    }

    public static String getValueOf(Map<String, Object> params, String documentId) {
        return params.containsKey(documentId) ? (String) params.get(documentId) : "";
    }
}
