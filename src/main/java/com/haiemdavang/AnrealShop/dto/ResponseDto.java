package com.haiemdavang.AnrealShop.dto;

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

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
