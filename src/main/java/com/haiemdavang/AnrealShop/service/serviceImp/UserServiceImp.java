package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.exception.AnrealShopException;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.repository.UserRepository;
import com.haiemdavang.AnrealShop.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements IUserService {
    private final UserRepository userRepository;

}
