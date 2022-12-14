package th.co.readypaper.billary.common.utils;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FileNameUtils {

    public static HttpHeaders attachment(String title) {
        String fileName = title + ".xlsx";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename(URLEncoder.encode(fileName, StandardCharsets.UTF_8))
                .build());

        return headers;
    }

}
