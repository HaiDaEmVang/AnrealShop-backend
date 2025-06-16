package com.haiemdavang.AnrealShop.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

    @Builder.Default
    private boolean isSuccess = true;

    @Builder.Default
    private int code = 200;

    private String message;

    private T data;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    public static <T> ResponseDto<T> success(T data, String message) {
        return ResponseDto.<T>builder()
                .data(data)
                .message(message)
                .isSuccess(true)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ResponseDto<T> error(String message, int code) {
        return ResponseDto.<T>builder()
                .message(message)
                .isSuccess(false)
                .code(code)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
