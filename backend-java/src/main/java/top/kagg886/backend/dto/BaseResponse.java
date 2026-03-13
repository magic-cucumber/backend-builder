package top.kagg886.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
public class BaseResponse<T> {
    private final int code;
    private final String message;
    private final T data;
    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, message, null);
    }

    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<>(0, "ok", data);
    }

    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @Data
    public static class Wrapped extends RuntimeException {
        private final int code;
        private final String message;

        public Wrapped(String message) {
            super(message);
            this.code = 10000;
            this.message = message;
        }
    }
}
