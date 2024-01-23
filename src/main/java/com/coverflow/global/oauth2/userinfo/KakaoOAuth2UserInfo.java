package com.coverflow.global.oauth2.userinfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(
            final Map<String, Object> attributes
    ) {
        super(attributes);
        System.out.println("attributes = " + attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");

        if (response == null) {
            return null;
        }
        return (String) response.get("email");
    }

    @Override
    public String getAge() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");

        if (response == null) {
            return null;
        }
        return (String) response.get("age");
    }

    @Override
    public String getGender() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");

        if (response == null) {
            return null;
        }
        return (String) response.get("gender");
    }
}
