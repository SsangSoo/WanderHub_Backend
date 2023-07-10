package wanderhub.server.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;
import wanderhub.server.global.response.ErrorResponder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MemberAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Exception 발생으로 인해 SecurityContext에 Authentication이 저장되지 않을 경우
    // AuthenticationException이 발생할 때 호출되는 핸들러

    // AuthenticationException(인증 예외)가 발생할 경우 호출, 처리하고자 하는 로직을 commence() 메서드로 구현하면 됨.
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (request.getHeader("Authorization") == null) {
            throw new CustomLogicException(ExceptionCode.TOKEN_WITHOUT);
        }
        ErrorResponder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED);
    }
}