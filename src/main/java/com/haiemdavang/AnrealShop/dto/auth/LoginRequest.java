package com.haiemdavang.AnrealShop.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "USERNAME_NOTBLANK")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "USERNAME_PATTERN")
    private String username;
    
    @NotBlank(message = "PASSWORD_NOTBLANK")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "PASSWORD_PATTERN")
    private String password;
}
