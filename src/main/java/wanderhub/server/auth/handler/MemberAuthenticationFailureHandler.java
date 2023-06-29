package wanderhub.server.auth.handler;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import wanderhub.server.auth.utils.ErrorResponder;
import wanderhub.server.global.response.ErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MemberAuthenticationFailureHandler implements AuthenticationFailureHandler {   // 인증 실패시 수행하는 필터 AuthenticationFailureHandler를 구현한다.
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.error("# Authentication failed: {}", exception.getMessage()); // error 메세지를 로그로 출력한다.

        sendErrorResponse(response);
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        Gson gson = new Gson(); // Error 정보가 담긴 객체(ErrorResponse)를 JSON 문자열로 변환하는데 사용
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED); // 401코드를 담은 ErrorResponse객체 생성
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);  // 응답 타입 JSON
        response.setStatus(HttpStatus.UNAUTHORIZED.value());        // HTTP Header에 상태코드(401)을 추가
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));    // toJson(매개변수, 타입지정) // Gson을 이용해서 ErrorResponse객체를 JSON 포맷 문자열로 변환 후, 출력스트림 생성

    }
}
