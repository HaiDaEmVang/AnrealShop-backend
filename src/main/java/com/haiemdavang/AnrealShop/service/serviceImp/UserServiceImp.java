package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.dto.user.ChangePasswordDto;
import com.haiemdavang.AnrealShop.dto.user.ProfileRequest;
import com.haiemdavang.AnrealShop.dto.user.RegisterRequest;
import com.haiemdavang.AnrealShop.dto.user.UserDto;
import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.exception.ConflictException;
import com.haiemdavang.AnrealShop.mapper.UserMapper;
import com.haiemdavang.AnrealShop.modal.entity.user.Role;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.modal.enums.RoleName;
import com.haiemdavang.AnrealShop.repository.UserRepository;
import com.haiemdavang.AnrealShop.security.SecurityUtils;
import com.haiemdavang.AnrealShop.service.IAddressService;
import com.haiemdavang.AnrealShop.service.ICartService;
import com.haiemdavang.AnrealShop.service.IShopService;
import com.haiemdavang.AnrealShop.service.IUserService;
import com.haiemdavang.AnrealShop.tech.mail.service.IMailService;
import com.haiemdavang.AnrealShop.utils.ApplicationInitHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements IUserService {
    private final UserRepository userRepository;
    private final RoleServiceImp roleService;
    private final IAddressService addressServiceImp;
    private final ICartService cartServiceImp;
    private final IShopService shopServiceImp;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final IMailService mailService;
    private final SecurityUtils securityUtils;


    @Override
    @Transactional
    public void resetPassword(String email, String password) {
        User user = findByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("USER_NOT_FOUND"));
    }

    @Override
    @Transactional
    public UserDto updateProfile(String email, ProfileRequest profileRequest) {
        User user = findByEmail(email);
        user = userMapper.updateUserFromProfileRequest(user, profileRequest);
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(String id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public void registerUser(RegisterRequest request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new ConflictException("EMAIL_ALREADY_EXISTS");
        }
        User user = userMapper.createUserFromRegisterRequest(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleService.getRoleByName(RoleName.USER);
        user.setAvatarUrl(ApplicationInitHelper.IMAGE_USER_DEFAULT);
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public UserDto findDtoByEmail(String username) {
        UserDto userDto =  userMapper.toUserDto(findByEmail(username));
        userDto.setHasShop(shopServiceImp.isExistByUserId(userDto.getId()));
        userDto.setAddress(addressServiceImp.findAddressPrimaryOrNull());
        userDto.setCartCount(cartServiceImp.countByUserId(userDto.getId()));
        return userDto;
    }

    @Override
    @Transactional
    public UserDto verifyEmail(String email, String code) {
        if (code == null || code.isEmpty() || !mailService.verifyOTP(code, email)) {
            throw new BadRequestException("INVALID_VERIFICATION_CODE");
        }
        User user = securityUtils.getCurrentUser();
        user.setVerify(true);
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updatePassword(ChangePasswordDto changePasswordDto) {
        User user = securityUtils.getCurrentUser();
        if (user.getPassword() != null && !user.getPassword().isEmpty() && !passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("OLD_PASSWORD_INCORRECT");
        }
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

}
