package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.redis.service.IRedisService;
import com.haiemdavang.AnrealShop.repository.CartRepository;
import com.haiemdavang.AnrealShop.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImp implements ICartService {
    private final CartRepository cartRepository;
    private final IRedisService redisService;
    private final String PREFIX_CART = "user:%s:cart:";


    @Override
    public int countByUserId(String userId) {
        String key = String.format(PREFIX_CART, userId);
        int cachedCount = redisService.getValue(key, -1);
        if (cachedCount != -1) return cachedCount;
        int count = cartRepository.countByUserId(userId);
        redisService.addValue(key, count);
        return count;
    }
}
