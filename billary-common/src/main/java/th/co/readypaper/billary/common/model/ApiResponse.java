package th.co.readypaper.billary.common.model;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private String status;
    private T data;

    public static <T> ApiResponse<T> of(String status, T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setStatus(status);
        res.setData(data);
        return res;
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setStatus(ApiStatus.SUCCESS.getVal());
        res.setData(data);
        return res;
    }

    public static <T> ApiResponse<T> failure(T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setStatus(ApiStatus.FAILURE.getVal());
        res.setData(data);
        return res;
    }

}
