package wanderhub.server.global.logger;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import wanderhub.server.auth.jwt.JwtTokenizer;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;

import javax.servlet.http.HttpServletRequest;

@Aspect             // 횡단관심사 적용.
@Slf4j              // 로그
//@Profile("local")   // yml의 local 선택
@Component
public class TokenValidationAspect {    // Token 검증 AOP
    public final JwtTokenizer jwtTokenizer;

    public TokenValidationAspect(JwtTokenizer jwtTokenizer) {
        this.jwtTokenizer = jwtTokenizer;
    }

    // 한 개 이상의 매개변수를 가지되, 첫번째 매개변수의 타입이 Principal인 메서드만 선택
    @Pointcut("execution(* wanderhub.server..*Controller.*(java.security.Principal, ..))")
    public void controller() {}


    // Controller
    @Before("controller()")
    public void accessTokenValidation() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String jws = request.getHeader("Authorization").replace("Bearer ", ""); //  request의 header에서 JWT를 얻음. // jws는 서명된 JWT를 의미함.
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        // 얻어온 토큰을 검증
        try {
            jwtTokenizer.verifySignature(jws, base64EncodedSecretKey);

        } catch (SignatureException se) {       // JWT의 서명이 올바르게 생성되지 않았거나 서명이 JWT 데이터와 일치하지 않는 경우 발생.
            throw new CustomLogicException(ExceptionCode.TOKEN_INVALID);
        } catch (ExpiredJwtException ee) {      // 만료로 인해 발생하는 예외
            throw new CustomLogicException(ExceptionCode.TOKEN_EXPIRED);
        }
    }

}