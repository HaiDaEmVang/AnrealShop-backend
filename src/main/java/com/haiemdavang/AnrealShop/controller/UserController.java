package com.haiemdavang.AnrealShop.controller;

import com.haiemdavang.AnrealShop.dto.user.ProfileRequest;
import com.haiemdavang.AnrealShop.dto.user.RegisterRequest;
import com.haiemdavang.AnrealShop.dto.user.UserDto;
import com.haiemdavang.AnrealShop.mapper.UserMapper;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.security.userDetails.UserDetailSecu;
import com.haiemdavang.AnrealShop.service.IUserService;
import io.jsonwebtoken.lang.Maps;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final IUserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok(Maps.of("message", "Tạo tài khoản thành công"));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetailSecu userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(userMapper.toUserDto(user));
    }


//
//    @GetMapping("/{id}")
//    public ResponseEntity<ResponseDto<UserDto>> getUserById(@PathVariable String id) {
//        User user = userService.findById(id);
//        UserDto userDto = userMapper.toUserDto(user);
//        ResponseDto<UserDto> response = ResponseDto.success(
//                userDto,
//                "Lấy thông tin người dùng thành công"
//        );
//        return ResponseEntity.ok(response);
//    }
    
    @PutMapping("/update-profile")
    public ResponseEntity<UserDto> updateProfile(@Valid @RequestBody ProfileRequest profileRequest, @AuthenticationPrincipal UserDetailSecu userDetails) {
        return ResponseEntity.ok(userService.updateProfile(userDetails.getEmail(), profileRequest));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Maps.of("message", "Xóa người dùng thành công"));
    }
}
