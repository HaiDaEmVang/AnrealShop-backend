package com.haiemdavang.AnrealShop.exception;

import com.haiemdavang.AnrealShop.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    Environment environment;

    @ExceptionHandler(AnrealShopException.class)
    public ResponseEntity<ResponseDto<?>> handleAnrealShopException(AnrealShopException ex) {
        log.error("AnrealShopException: {}", ex.getMessage());
        String errorMessage = environment.getProperty(ex.getMessage(), "Loi khong xac dinh");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .isSuccess(false)
                .message(errorMessage).build());
    }

    @ExceptionHandler(UnAuthException.class)
    public ResponseEntity<ResponseDto<?>> authException(UnAuthException ex) {
        log.error("AnrealShopException: {}", ex.getMessage());
        String errorMessage = environment.getProperty(ex.getMessage(), "Loi khong xac dinh");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .isSuccess(false)
                .message(errorMessage).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<?>> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        assert ex.getBindingResult() != null;
        String errorMessage = "Validation failed: " + ex.getBindingResult().getAllErrors().stream()
                .map(err -> environment.getProperty(Objects.requireNonNull(err.getDefaultMessage())))
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(ResponseDto.builder().message(errorMessage).isSuccess(false).code(400).build());
    }



}
