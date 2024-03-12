package com.coverflow.global.oauth2.handler;

import com.coverflow.global.oauth2.CustomOAuth2User;
import com.coverflow.global.util.AesUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

/**
 * 소셜 로그인 성공 후 행동을 정하는 핸들러
 * 자체 인가 코드 발행 후 프론트로 리다이렉트
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    @Transactional
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication
    ) throws IOException {
        log.info("OAuth2 Login 성공!");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 인가 코드 발행
        String code;
        String role;
        try {
            code = AesUtil.encrypt(String.valueOf(oAuth2User.getMemberId()));
            role = AesUtil.encrypt(String.valueOf(oAuth2User.getRole()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String targetUrl = createURI(code, role).toString();

        // 프론트의 토큰 관리 페이지로 리다이렉트
        response.sendRedirect(targetUrl);
    }

    // 자체 인가 코드를 URL에 담아 리다이렉트
    private URI createURI(
            final String code,
            final String role
    ) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

        queryParams.add("code", code);
        queryParams.add("role", role);
        log.info("인가 코드 담기 성공");

        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("15.165.1.48")
//               .scheme("https")
//               .host("coverflow.co.kr")
                .path("/auth/token")
                .queryParams(queryParams)
                .build()
                .encode()
                .toUri();
    }
}