package wanderhub.server.auth.jwt.refreshtoken.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@RedisHash(value = "refreshToken")
public class RefreshToken {

    @Id
    private String email;

    @Indexed // 필드 값으로 데이터 찾을 수 있게 하는 어노테이션
    private String refreshToken;

    @TimeToLive
    private int expired;

    public RefreshToken(String email, String refreshToken, int expired) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.expired = expired;
    }
}