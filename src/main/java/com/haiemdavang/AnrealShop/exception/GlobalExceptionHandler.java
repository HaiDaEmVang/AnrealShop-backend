package com.haiemdavang.AnrealShop.exception;

import com.haiemdavang.AnrealShop.dto.common.ErrorResponseDto;
import com.haiemdavang.AnrealShop.dto.common.ItemError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final Environment environment;

    private ErrorResponseDto buildErrorResponse(HttpStatus status, String message, List<ItemError> details) {
        return ErrorResponseDto.builder()
                .code(String.valueOf(status.value()))
                .message(message)
                .details(details)
                .build();
    }

    @ExceptionHandler(AnrealShopException.class)
    public ResponseEntity<ErrorResponseDto> handleAnrealShopException(AnrealShopException ex) {
        log.error("AnrealShopException: {}", ex.getMessage());
        String errorMessage = environment.getProperty(ex.getMessage(), "Lỗi không xác định từ AnrealShop.");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, null));
    }

    @ExceptionHandler(UnAuthException.class)
    public ResponseEntity<ErrorResponseDto> authException(UnAuthException ex) {
        log.error("UnAuthException: {}", ex.getMessage());
        String errorMessage = environment.getProperty(ex.getMessage(), "Lỗi xác thực: Bạn không có quyền truy cập.");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(buildErrorResponse(HttpStatus.UNAUTHORIZED, errorMessage, null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        List<ItemError> details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ItemError(error.getField(),
                        environment.getProperty(Objects.requireNonNull(error.getDefaultMessage()), error.getDefaultMessage())))
                .collect(Collectors.toList());

        String errorMessage = "Dữ liệu đầu vào không hợp lệ.";
        return ResponseEntity
                .badRequest()
                .body(buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, details));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("Bad credentials: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(buildErrorResponse(HttpStatus.UNAUTHORIZED, "Tên đăng nhập hoặc mật khẩu không đúng.", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralException(Exception ex) {
        log.error("Đã xảy ra lỗi không mong muốn: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau.", null));
    }
}