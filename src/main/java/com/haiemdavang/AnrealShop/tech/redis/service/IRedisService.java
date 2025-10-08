package com.haiemdavang.AnrealShop.tech.redis.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface IRedisService {
    <T> void addValue(String key, T value);
    <T> void addValue(String key, T value, long time, TimeUnit timeUnit);
    <T> void addValue(Map<String , T> values);
    <T> T getValue(String key);
    <T> T getValue(String key, T defaultValue);

    void del(String key);
    boolean isExists(String key);
    <T> HashMap<String, T> getAll();
}
