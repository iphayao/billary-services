package th.co.readypaper.billary.common.model;

import lombok.Data;

import java.util.List;

@Data
public class ResultPage<T> {
    List<T> results;
    int page;
    int limit;
    int size;

    public static <T> ResultPage<T> of(List<T> data, int page, int limit, int size) {
        ResultPage<T> resultPage = new ResultPage<>();
        resultPage.setResults(data);
        resultPage.setPage(page);
        resultPage.setLimit(limit);
        resultPage.setSize(size);
        return resultPage;
    }

}
