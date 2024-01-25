package com.coverflow.global.oauth2.handler;

import com.coverflow.global.jwt.service.JwtService;
import com.coverflow.global.oauth2.CustomOAuth2User;
import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.MemberRepository;
import com.coverflow.member.domain.Role;
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

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication
    ) throws IOException {
        log.info("OAuth2 Login 성공!");
        final CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 로그인 성공하면
        // 리프레쉬 토큰 유무에 관계없이(첫 사용자, 기존 사용자 모두)
        // 액세스 + 리프레쉬 토큰 발급
        // 즉, 리프레쉬 토큰은 1회용(보안 강화)
        final String accessToken = jwtService.createAccessToken(String.valueOf(oAuth2User.getMemberId()));
        final String refreshToken = jwtService.createRefreshToken();
        final String targetUrl = createURI(accessToken, refreshToken).toString();

        // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
        if (oAuth2User.getRole() == Role.GUEST) {
            final Member member = memberRepository.findByMemberId(oAuth2User.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

            member.authorizeMember();
        }
        response.sendRedirect(targetUrl); // 프론트의 추가 정보 입력 폼으로 리다이렉트
    }

//    // 소셜 로그인 후
//    // 리프레쉬 토큰 유무에 관계없이(첫 사용자, 기존 사용자 모두)
//    // 액세스 + 리프레쉬 토큰 발급
//    // 즉, 리프레쉬 토큰은 1회용(보안 강화)
//    private void loginSuccess(
//            final HttpServletResponse response,
//            final CustomOAuth2User oAuth2User
//    ) {
//        final String accessToken = jwtService.createAccessToken(String.valueOf(oAuth2User.getMemberId()));
//        final String refreshToken = jwtService.createRefreshToken();
//
//        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
//        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);
//
//        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
//        jwtService.updateRefreshToken(oAuth2User.getMemberId(), refreshToken);
//    }

    // JWT를 쿼리 파라미터로 담아 리다이렉트
    private URI createURI(String accessToken, String refreshToken) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);

        return UriComponentsBuilder
                .newInstance()
                .scheme("https")
                .host("coverflow.co.kr")
                .path("/auth/token")
                .queryParams(queryParams)
                .build()
                .encode()
                .toUri();
    }
}