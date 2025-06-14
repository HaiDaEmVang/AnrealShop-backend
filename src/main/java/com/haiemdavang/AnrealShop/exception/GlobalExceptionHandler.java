package com.haiemdavang.AnrealShop.exception;

import com.haiemdavang.AnrealShop.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final Environment environment;

    @ExceptionHandler(AnrealShopException.class)
    public ResponseEntity<ResponseDto<?>> handleAnrealShopException(AnrealShopException ex) {
        log.error("AnrealShopException: {}", ex.getMessage());
        String errorMessage = environment.getProperty(ex.getMessage(), "Loi khong xac dinh");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResponseDto.error(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(UnAuthException.class)
    public ResponseEntity<ResponseDto<?>> authException(UnAuthException ex) {
        log.error("AnrealShopException: {}", ex.getMessage());
        String errorMessage = environment.getProperty(ex.getMessage(), "Loi khong xac dinh");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ResponseDto.error(errorMessage, HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<?>> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        assert ex.getBindingResult() != null;
        String errorMessage = "Validation failed: " + ex.getBindingResult().getAllErrors().stream()
                .map(err -> environment.getProperty(Objects.requireNonNull(err.getDefaultMessage())))
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(
                ResponseDto.error(errorMessage, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDto<?>> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("Bad credentials: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ResponseDto.error("Sai tên đăng nhập hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED.value()));
    }


}
