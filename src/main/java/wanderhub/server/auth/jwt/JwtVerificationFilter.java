package wanderhub.server.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import wanderhub.server.auth.utils.CustomAuthorityUtils;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter { // request당 한 번만 실행되는 SecurityFilter를 상속

    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String accessToken = request.getHeader("Authorization").replace("Bearer ", "");

            if (!StringUtils.hasText(accessToken)) {
                throw new CustomLogicException(ExceptionCode.TOKEN_WITHOUT);
            }
            Map<String, Object> claims = verifyJws(accessToken);
            setAuthenticationToContext(claims);
            filterChain.doFilter(request, response);
    }

    // true일 경우 해당 필터 스킵.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");      // Header에서 authorization을 얻어온다.

        // 값이 null이거나, 'Bearer'로 시작하지 않으면 동작하지 않는다.
            // Authorization header에 포함되지 않았으면, 자격증명이 필요하지 않은 리소스에 대한 요청으로 판단.
            // JWT가 필요한데, 값이 없다면, 다른 Security Filter를 거쳐 Exception을 던질 것이다.
        return authorization == null || !authorization.startsWith("Bearer");
    }


    private Map<String, Object> verifyJws(HttpServletRequest request) {
            String jws = request.getHeader("Authorization").replace("Bearer ", "");     //  request의 header에서 JWT를 얻음. // jws는 서명된 JWT를 의미함.
            String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());  // 서명을 검증하기위한 SecretKey를 얻는다.
            Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody(); // Claims를 파싱한다.

            return claims;
    }

    private Map<String, Object> verifyJws(String accessToken) {
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());  // 서명을 검증하기위한 SecretKey를 얻는다.
        Map<String, Object> claims = jwtTokenizer.getClaims(accessToken, base64EncodedSecretKey).getBody(); // Claims를 파싱한다.

        return claims;
    }



    // Authentication 객체를 SecurityContext에 저장하기 위한 메서드
    // 이 메서드가 있기 때문에, 컨트롤러의 매개변수로 Principal을 받을 수 있나..?
    private void setAuthenticationToContext(Map<String, Object> claims) {
        String username = (String) claims.get("username");  // JWT에서 파싱한 Claims에서 'username'을 얻는다.
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List) claims.get("roles"));  // JWT의 Claims에서 얻은 권한 정보를 기반으로 권한리스트를 얻는다.
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);   //
        SecurityContextHolder.getContext().setAuthentication(authentication);   // SecurityContext에 Authentication 객체 저장
    }
}
