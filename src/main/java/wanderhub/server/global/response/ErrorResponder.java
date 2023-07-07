package wanderhub.server.global.response;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorResponder {
        // ErrorResponse를 클라이언트에게 전송하기 위한 클래스
        public static void sendErrorResponse(HttpServletResponse response, HttpStatus status) throws IOException {
            Gson gson = new Gson(); // Error 정보가 담긴 객체(ErrorResponse)를 JSON 문자열로 변환하는데 사용
            ErrorResponse errorResponse = ErrorResponse.of(status); // 401코드를 담은 ErrorResponse객체 생성
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);  // 응답 타입 JSON
            response.setStatus(HttpStatus.UNAUTHORIZED.value());        // HTTP Header에 상태코드(401)을 추가
            response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));    // toJson(매개변수, 타입지정) // Gson을 이용해서 ErrorResponse객체를 JSON 포맷 문자열로 변환 후, 출력스트림 생성
        }

}