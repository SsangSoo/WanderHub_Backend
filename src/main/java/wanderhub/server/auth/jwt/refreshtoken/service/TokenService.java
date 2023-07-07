package wanderhub.server.auth.jwt.refreshtoken.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wanderhub.server.auth.jwt.JwtTokenizer;
import wanderhub.server.auth.utils.CustomAuthorityUtils;
import wanderhub.server.auth.utils.RedisUtils;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Slf4j
public class TokenService {
    private final CustomAuthorityUtils customAuthorityUtils;
    private final RedisUtils redisUtils;
    private final JwtTokenizer jwtTokenizer;

    public TokenService(CustomAuthorityUtils customAuthorityUtils, RedisUtils redisUtils, JwtTokenizer jwtTokenizer) {
        this.customAuthorityUtils = customAuthorityUtils;
        this.redisUtils = redisUtils;
        this.jwtTokenizer = jwtTokenizer;
    }

    // 로그인 혹은 가입시 호출되는 메서드.
    public String saveRefreshToken(String email, String refreshToken) {
        long refreshTokenTime = jwtTokenizer.getRefreshTokenExpirationMinutes() * 60 * 1000;// 1440 * 60 * 1000
        redisUtils.setData(email + ":refreshToken", refreshToken, refreshTokenTime);
        String savedRefreshToken = redisUtils.getData(email + ":refreshToken");
        return savedRefreshToken;
    }

    // AccessToken 재발급 메서드
    public String reissueAccessToken(String email, String refreshToken) {
        // RefreshToken 조회
        Optional<String> optionalMemberRefreshToken = Optional.ofNullable(redisUtils.getData(email + ":refreshToken"));
        // refreshToken 없으면 예외 발생.
        String memberRefreshToken = optionalMemberRefreshToken.orElseThrow(() -> new CustomLogicException(ExceptionCode.REFRESH_TOKNE_WITHOUT));
        // 동일하면, 액세스 토큰 재발급
        if(memberRefreshToken.equals(refreshToken)) {
            List<String> authorities = customAuthorityUtils.createRoles(email);
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", email);
            claims.put("roles", authorities);

            String subject = email;
            Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
            String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
            String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

            return "Bearer " + accessToken;

        } else {    // RefreshToken 값이 다르면, 예외 발생
            throw new CustomLogicException(ExceptionCode.REFRESH_TOKEN_INVALID);
        }

    }

    // 로그아웃
    public void logoutService(String email, String accessToken) {
        // 이메일과 연결된 리프레시 토큰 삭제
        redisUtils.deleteData(email + ":refreshToken");
        // 액세스 토큰은 아직 유효하므로, 유효한 시간까지 블랙리스트로 넣어두고, 시간이 될 때 삭제하도록 한다.
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Jws<Claims> claims = jwtTokenizer.getClaims(accessToken, base64EncodedSecretKey);
        Date expiration = claims.getBody().getExpiration();
        long millis = expiration.getTime() - System.currentTimeMillis(); // 남은 시간 millisSecond(밀리초) 
        redisUtils.setData(email+":logOut", accessToken, millis);   // 밀리초 이후에 삭제.
    }




    // AccessToken 블랙리스트 검증
        // Logout한 유저라면, 해당토큰을 가졌을 때, 사용하지 못 하도록 예외 발생해야한다.
    // 액세스 토큰에서 email 추출 => Principal로 하려해봤자, 인증되지 않은 토큰이라면 그 전에 예외처리 발생하므로,
    // 아예 해당 메서드에서 모든 로직 처리해야 됨.
    public void verificationLogOutToken(HttpServletRequest request) {
        // 액세스 토큰
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");

        // 토큰에서 email 추출
        String email = jwtTokenizer.getClaims(accessToken, jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey())).getBody().getSubject();

        // RedisUtils에서 LogoutToken을 얻어온다.
        String blackListAccessToken = redisUtils.getData(email + ":logOut");

        // 만약 null이 아니라면, 즉 값이 있다면, 블랙리스트 토큰 이므로 예외처리한다.
        if(blackListAccessToken!=null) {
            throw new CustomLogicException(ExceptionCode.LOGOUT_TOKEN);
        } // else는 필요없음. null이면 통과.
    }

}
