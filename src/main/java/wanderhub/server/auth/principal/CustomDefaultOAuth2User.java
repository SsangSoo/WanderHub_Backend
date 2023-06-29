package wanderhub.server.auth.principal;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import wanderhub.server.auth.oauth.OAuth2Attribute;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomDefaultOAuth2User extends DefaultOAuth2User {

    private OAuth2Attribute oAuth2Attribute;

    public CustomDefaultOAuth2User(Collection<? extends GrantedAuthority> authorities,
                                   Map<String, Object> attributes,
                                   String nameAttributeKey,
                                   OAuth2Attribute oAuth2Attribute) {
        super(authorities, attributes, nameAttributeKey);
        this.oAuth2Attribute = oAuth2Attribute;
    }
}