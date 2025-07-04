package trinh_be.common.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <E> ApiResponse<E> success(E data) {
        return ApiResponse.<E>builder()
                .success(true)
                .message("Success")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <E> ApiResponse<E> error(Exception e) {
        return ApiResponse.<E>builder()
                .success(false)
                .message(e.getMessage())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

