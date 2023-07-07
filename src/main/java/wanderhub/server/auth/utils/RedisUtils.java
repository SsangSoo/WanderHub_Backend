package wanderhub.server.auth.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    // Redis에 저장 // 동일한 Key 덮어 씀
    public void setData(String key, String value, Long expiredTime){
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS);
    }

    // Redis에서 조회
    public String getData(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    // Redis에서 삭제
    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}
