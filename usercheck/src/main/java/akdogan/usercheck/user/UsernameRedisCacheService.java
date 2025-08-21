package akdogan.usercheck.user;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import akdogan.usercheck.common.RedisCacheService;

@Service
public class UsernameRedisCacheService extends RedisCacheService{

    public UsernameRedisCacheService(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

}