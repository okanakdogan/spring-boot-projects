package akdogan.usercheck.common;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheService {
    
    private final StringRedisTemplate redisTemplate;

    public RedisCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheValue(String value) {
        redisTemplate.opsForValue().set("string:" + value, "true", 10, TimeUnit.MINUTES);
    }

    public boolean checkKeyExists(String key) {
        return redisTemplate.hasKey("string:" + key);
    }

    public void removeKey(String key) {
        redisTemplate.delete("string:" + key);
    }
}