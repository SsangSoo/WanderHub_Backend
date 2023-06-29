package wanderhub.server.auth.oauth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)  // private 접근제어자를 가진 빌더패턴 생성
@Getter
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String name;
    private String picture;

    // 벤더사에 따라 호출하는 메서드를 다르게 함.
        // 호출메서드는 각 벤더사에 맞게 OAuth2 유저를 생성하도록 되어있다.
    public static OAuth2Attribute of(String provider, String attributeKey,
                                     Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(attributeKey, attributes);
            case "kakao":
                return ofKakao("email", attributes);
            default:
                throw new RuntimeException();
        }
    }

    // 구글 로그인 OAuth2 유저 사용자.
    private static OAuth2Attribute ofGoogle(String attributeKey,
                                            Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    // 카카오 로그인 OAuth2 유저 사용자
    private static OAuth2Attribute ofKakao(String attributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .name((String) kakaoProfile.get("nickname"))    // 카카오 계정으로 닉네임 추출
                .email((String) kakaoAccount.get("email"))      // 카카오 계정으로 이메일을 추출
                .attributes(kakaoAccount)                       // Map형태의 kakaoAccount를 OAuth2Attribute 속성에 추가.
                .attributeKey(attributeKey)                     // attributeKey를 속성에 추가.
                .build();
    }


    // OAuth2User 객체에 넣어주기 위해서 내부 속성을 Map으로 변환해서 값들을 반환해준다.
    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("name", name);
        map.put("email", email);
        map.put("picture", picture);

        return map;
    }
}
