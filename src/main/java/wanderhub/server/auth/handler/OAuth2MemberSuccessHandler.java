package wanderhub.server.auth.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import wanderhub.server.auth.jwt.JwtTokenizer;
import wanderhub.server.auth.jwt.refreshtoken.service.TokenService;
import wanderhub.server.auth.utils.CustomAuthorityUtils;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.member.service.MemberService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;

@RequiredArgsConstructor
public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler { //  Redirect를 손쉽게 할 수 있게 SimpleUrlAuthenticationSuccessHandler 상속
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberService memberService;
    private final TokenService refreshTokenService;

    // 인증에 성공한 사용자 정보를 전달하는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var oAuth2User = (OAuth2User) authentication.getPrincipal();                // 인증된 객체로부터 OAuth2User를 얻어온다.
        String email = String.valueOf(oAuth2User.getAttributes().get("email"));     // OAuth2User에서 이메일 주소를 얻어온다.
        List<String> authorities = authorityUtils.createRoles(email);               // 얻어온 이메일로 사용자 권한을 생성한다. // admin이 있기때문에, 사용
        // 이메일로 멤버가 있는지 확인한다.
        Optional<Member> member = memberService.findByEmail(email); // 이메일을 통해서 사용자를 찾아온다.
        boolean newbie;
        if(!member.isPresent()) {   // 없다면 이메일을 통해서 Member를 생성한다.
            saveMember(email);      // 이메일을 통해 User생성
            newbie = true;          // 새로 가입하는 사람
        } else {                                                    // 새로 가입하는 사람 아니라면, newbie는 false
            memberService.verificationActiveMember(member.get());   //  기존 사용자가 활동중인지 아닌지 검증한다.
            newbie = false;
        }   // if-else 통과시 이미 있는 멤버.
        redirect(request, response, email, authorities, newbie);    // AccessToken과 Refresh Token을 생성해서 전달하는 Redirect
    }

    private void saveMember(String email) {
            Member member = new Member(email, true);                 // 멤버가 생성됨.
            memberService.createMember(member);                             // member를 DB에 저장
    }

    // 토큰 정보를 얻어 프론트엔드로 리다이렉트 해주는 메서드

    private void redirect(HttpServletRequest request, HttpServletResponse response, String email, List<String> authorities, boolean newbie) throws IOException {
        String accessToken = delegateAccessToken(email, authorities);    // accessToken을 생성
        String refreshToken = delegateRefreshToken(email);               // refreshToken을 생성

        // 프론트엔드 애플리케이션 쪽의 URL을 생성한다.
            // creatURI의 UriComponentsBuilder를 이용해서 AccessToken과 RefreshToken을 포함한 URL을 생성함.
        String uri = createURI(accessToken, refreshToken, newbie).toString();
        getRedirectStrategy().sendRedirect(request, response, uri);         // SimpleUrlAuthenticationSuccessHandler에서 제공하는 메서드

    }
    // 유저와 권한정보를 얻어 AccessToken을 생성
    private String delegateAccessToken(String email, List<String> authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", email);
        claims.put("roles", authorities);

        String subject = email;
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
        return accessToken;
    }

    // RefreshToken 생성
    private String delegateRefreshToken(String email) {
        String subject = email;
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
        return refreshTokenService.saveRefreshToken(email, refreshToken);   // 레디스에 저장 후, 반환.
    }



    private URI createURI(String accessToken, String refreshToken, boolean newbie) {
        // MultiValueMap는 Map을 확장하여 키와 여러개의 값 연결 가능.
        // LinkedMultiValueMap은 MultiValueMap 인터페이스의 구현체 중 하나.
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);
        queryParams.add("newbie", String.valueOf(newbie));


           return UriComponentsBuilder
                   .newInstance()
                   .scheme("http")
                   .host("localhost")
                   .port(8080)
                   .path("/receive-token")
                   .queryParams(queryParams)
                   .build()
                   .toUri();
       }
//       return UriComponentsBuilder
//               .newInstance()
//               .scheme("https")
//               .host("whback.kro.kr")
//               .port(443)
//               .path("/receive-token")
//               .queryParams(queryParams)
//               .build()
//               .toUri();
//   }

}
