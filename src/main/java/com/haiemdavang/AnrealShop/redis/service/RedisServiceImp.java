package com.haiemdavang.AnrealShop.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImp implements IRedisService{
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> void addValue(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public <T> void addValue(String key, T value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    @Override
    public <T> T getValue(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> void addValue(Map<String, T> values) {
        for (Map.Entry<String, T> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            redisTemplate.opsForValue().set(key, value);
        }
    }

    @Override
    public void del(String key) {
        if(isExists(key)) {
            redisTemplate.delete(key);
        }
    }

    @Override
    public boolean isExists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public <T> HashMap<String, T> getAll() {
        Set<String> keys = redisTemplate.keys("*");
        HashMap<String, T> allData = new HashMap<>();
        for (String key : keys) {
            T value = (T) redisTemplate.opsForValue().get(key);
            allData.put(key, value);
        }
        return allData;
    }

}
