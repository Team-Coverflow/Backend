package com.coverflow.global.oauth2;

import com.coverflow.member.domain.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final UUID memberId;
    private final Role role;

    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's "name" from
     *                         {@link #getAttributes()}
     */
    public CustomOAuth2User(
            final Collection<? extends GrantedAuthority> authorities,
            final Map<String, Object> attributes,
            final String nameAttributeKey,
            final UUID memberId,
            final Role role
    ) {
        super(authorities, attributes, nameAttributeKey);
        this.memberId = memberId;
        this.role = role;
    }
}
