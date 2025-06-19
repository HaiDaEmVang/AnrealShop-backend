package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.dto.auth.Oauth2.Oauth2UserInfo;
import com.haiemdavang.AnrealShop.dto.user.ProfileRequest;
import com.haiemdavang.AnrealShop.dto.user.RegisterRequest;
import com.haiemdavang.AnrealShop.dto.user.UserDto;
import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.exception.ConflictException;
import com.haiemdavang.AnrealShop.mapper.UserMapper;
import com.haiemdavang.AnrealShop.modal.entity.user.Role;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.modal.enums.RoleName;
import com.haiemdavang.AnrealShop.repository.RoleRepository;
import com.haiemdavang.AnrealShop.repository.UserRepository;
import com.haiemdavang.AnrealShop.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isExitsts(String email) {
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
    public void createUserFromOauth2(Oauth2UserInfo info) {
        if (info == null || info.getEmail() == null) {
            throw new BadRequestException("INVALID_OAUTH2_USER_INFO");
        }
        User newUser = userMapper.createUserFromOauth2UserInfo(info);
        String randomPassword = generateRandomPassword();
        newUser.setPassword(passwordEncoder.encode(randomPassword));
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
        user.setAvatarUrl("https://res.cloudinary.com/dqogp38jb/image/upload/v1750060824/7309681_msx5j1.jpg");
        Role role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new BadRequestException("ROLE_NOT_FOUND"));
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public UserDto findDtoByEmail(String username) {
        return userMapper.toUserDto(findByEmail(username));
    }

    private String generateRandomPassword() {
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
