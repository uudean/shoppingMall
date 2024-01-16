package com.soloproject.shoppingmall.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Object> redisBlackListTemplate;

    public void set(String key, Object object, long minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(object.getClass()));
        redisTemplate.opsForValue().set(key, object, minutes, TimeUnit.MINUTES);
    }

    public void setIncrement(String key, long num) {

        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().increment(key, num);

    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void setBlackList(String key, Object object, long minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(object.getClass()));
        redisTemplate.opsForValue().set(key, object, minutes, TimeUnit.MILLISECONDS);
    }

    public Object getBlackList(String key) {
        return redisBlackListTemplate.opsForValue().get(key);
    }

    public boolean deleteBlackList(String key) {
        return Boolean.TRUE.equals(redisBlackListTemplate.delete(key));
    }

    public boolean hasKeyBlackList(String key) {
        return Boolean.TRUE.equals(redisBlackListTemplate.hasKey(key));
    }
}
