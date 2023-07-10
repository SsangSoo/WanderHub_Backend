package wanderhub.server.auth.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service    // OAuth2 인증으로 사용자 정보를 가져오는 역할을 하는 클래스
public class CustomOAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2UserRequest는 OAuth2 인증에 필요한 정보를 포함하고 있다.
        // 해당 매개변수를 이용해서 DefaultOAuth2UserService 객체를 생성한다.
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        // 생성한 DefaultOAuth2UserService객체로 OAuth2 제공자로부터 사용자 정보를 가져오는 역할을 한다.
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // userRequest에서 클라이언트 등록된 벤더(구글,카카오)가 무엇인지 식별한다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // userRequest에서 클라이언트 벤더에서 인증된 사용자의 정보를 가져온다.
            // 이름 혹은 이메일
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService를 사용하여 가져온 OAuth2User 정보로 OAuth2Attribute 객체를 만든다.
            // OAuth2인증 제공자의 등록ID, 사용자 이름, 사용자 속성 포함
        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // OAuth2Attribute 정보 출력(로그로)
//        log.info("{}", oAuth2Attribute);

        // OAuth2Attribute를 Map형식으로 변환한다.
        var memberAttribute = oAuth2Attribute.convertToMap();

        // DefaultOAuth2User를 생성해서 반환한다.
        // ROLE USER의 역할을 가진 SimpleGrantedAuthority 객체와 email을 사용하여 OAuth2User를 생성한다.
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                memberAttribute, "email");
    }
}
