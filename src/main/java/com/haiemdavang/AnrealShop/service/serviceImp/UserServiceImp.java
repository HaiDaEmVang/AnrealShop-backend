package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.dto.auth.Oauth2.OAuth2UserInfo;
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
import com.haiemdavang.AnrealShop.service.IAddressService;
import com.haiemdavang.AnrealShop.service.ICartService;
import com.haiemdavang.AnrealShop.service.IShopService;
import com.haiemdavang.AnrealShop.service.IUserService;
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

    @Override
    public boolean isExists(String email) {
        return !userRepository.existsByEmail(email);
    }

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
                .orElseThrow(()-> new BadRequestException("USER_NOT_FOUND"));
    }

    @Override
    @Transactional
    public void createUserFromOauth2(OAuth2UserInfo info) {
        if (info == null || info.getEmail() == null) {
            throw new BadRequestException("INVALID_OAUTH2_USER_INFO");
        }
        User newUser = userMapper.createUserFromOauth2UserInfo(info);
        String randomPassword = generateRandomPassword();
        newUser.setPassword(passwordEncoder.encode(randomPassword));

        Role role = roleService.getRoleByName(RoleName.USER);
        newUser.setRole(role);
        userRepository.save(newUser);
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
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public UserDto findDtoByEmail(String username) {
        UserDto userDto =  userMapper.toUserDto(findByEmail(username));
        userDto.setHasShop(shopServiceImp.isExistByUserId(userDto.getId()));
        userDto.setAddress(addressServiceImp.findAddressPrimary());
        userDto.setCartCount(cartServiceImp.countByUserId(userDto.getId()));
        return userDto;
    }

    private String generateRandomPassword() {
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
