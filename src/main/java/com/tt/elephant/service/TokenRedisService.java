package com.tt.elephant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


public class TokenRedisService {
    RedisTemplate<String, Object> redisTemplate;

    /**
     * save token to redis db
     * @param key
     * @param value
     * @param expire
     */
    public void save(String key, String value, int expire) {
        redisTemplate.opsForValue().set(key, value);

    }
}
