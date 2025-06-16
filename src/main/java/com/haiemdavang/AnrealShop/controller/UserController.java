package com.haiemdavang.AnrealShop.controller;

import com.haiemdavang.AnrealShop.dto.common.ResponseDto;
import com.haiemdavang.AnrealShop.dto.user.ProfileRequest;
import com.haiemdavang.AnrealShop.dto.user.RegisterRequest;
import com.haiemdavang.AnrealShop.dto.user.UserDto;
import com.haiemdavang.AnrealShop.mapper.UserMapper;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.security.userDetails.UserDetailSecu;
import com.haiemdavang.AnrealShop.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ResponseDto<String>> register(@Valid @RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok(ResponseDto.success("", "Tạo tài khoản thành công"));
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseDto<UserDto>> getCurrentUser(@AuthenticationPrincipal UserDetailSecu userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        UserDto userDto = userMapper.toUserDto(user);
        ResponseDto<UserDto> response = ResponseDto.success(userDto,"Lấy thông tin người dùng thành công");
        return ResponseEntity.ok(response);
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
    
    @PutMapping("/profile")
    public ResponseEntity<ResponseDto<UserDto>> updateProfile(@Valid @RequestBody ProfileRequest profileRequest, @AuthenticationPrincipal UserDetailSecu userDetails) {
        UserDto userDto = userService.updateProfile(userDetails.getEmail(), profileRequest);
        ResponseDto<UserDto> response = ResponseDto.success(userDto,"Cập nhật thông tin người dùng thành công");
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        
        ResponseDto<Void> response = ResponseDto.success(
                null,
                "Xóa người dùng thành công"
        );
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
