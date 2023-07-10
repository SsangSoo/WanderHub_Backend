package wanderhub.server.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component  // 빈으로 등록
public class JwtTokenizer {

    @Getter
    @Value("${jwt.key.secret}")
    private String secretKey;   // yml의 {jwt.key}를 불러온다.

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;   // yml의 {jwt.access-token-expiration-minutes}의 값을 불러온다.

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;  // yml의 {jwt.refresh-token-expiration-minutes}의 값을 불러언다.

    // 순수 Text 형태의 SecretKey의 byte[]를 Base64 형식의 문자열로 인코딩해준다.
    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // 인증된 사용자에게 JWT를 최초로 발급해주기 위한 JWT 생성메서드
    public String generateAccessToken(Map<String, Object> claims,   // 인증된 사용자와 관련된 정보
                                      String subject,               // JWT의 제목
                                      Date expiration,              // 만료시간
                                      String base64EncodedSecretKey) {  // 시크릿 키
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);   // 알고리즘을 통해서 암호화된 Key

        return Jwts.builder()
                .setClaims(claims)                              // 사용자와 관련된 정보 추가
                .setSubject(subject)                            // JWT 제목
                .setIssuedAt(Calendar.getInstance().getTime())  // JWT의 발행일자
                .setExpiration(expiration)                      // 만료일자
                .signWith(key)                                  // 서명을 위한 Key객체 설정
                .compact();                                     // JWT생성 & 직렬화
    }

    // AccessToken이 만료되었을 경우, 새로 생성할 수 있게 해주는 Refresh Token 생성
    // Access Token을 생성해주는 역할을 하므로, claims가 없어도 됨.
    public String generateRefreshToken(String subject,
                                       Date expiration,
                                       String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jws<Claims> claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jws);   // JWT를 검증하고 예외를 발생시키는 메서드
        } catch (SignatureException se) {
            throw new CustomLogicException(ExceptionCode.TOKEN_INVALID);
        } catch (ExpiredJwtException ee) {
            throw new CustomLogicException(ExceptionCode.TOKEN_EXPIRED);
        }
        return claims;
    }

    // JWT의 만료 일시를 지정하기 위한 메서드로 JWT 생성시 사용된다.
    public Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();

        return expiration;
    }

    // JWT 서명에 사용할 SecretKey를 생성해준다.
    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);   // 인코딩된 SecretKey를 디코딩한 후 byte array를 반환한다.
        Key key = Keys.hmacShaKeyFor(keyBytes); // 디코딩된 secretKey를 HMAC 알고리즘을 적용하여 (java.security.Key)겍체를 생성한다.

        return key;
    }

}
