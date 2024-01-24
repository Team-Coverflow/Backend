package com.coverflow.global.oauth2.presentation;

import com.coverflow.global.jwt.service.JwtService;
import com.coverflow.global.oauth2.CustomOAuth2User;
import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.MemberRepository;
import com.coverflow.member.domain.Role;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @GetMapping("/token")
    public ResponseEntity<?> generateToken(
            final HttpServletResponse response,
            final Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성

        // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
        if (oAuth2User.getRole() == Role.GUEST) {
            final Member member = memberRepository.findByMemberId(oAuth2User.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

            member.authorizeMember();
        }

        return ResponseEntity.ok(response);
    }

    // 소셜 로그인 후
    // 리프레쉬 토큰 유무에 관계없이(첫 사용자, 기존 사용자 모두)
    // 액세스 + 리프레쉬 토큰 발급
    // 즉, 리프레쉬 토큰은 1회용(보안 강화)
    private void loginSuccess(
            final HttpServletResponse response,
            final CustomOAuth2User oAuth2User
    ) {
        final String accessToken = jwtService.createAccessToken(String.valueOf(oAuth2User.getMemberId()));
        final String refreshToken = jwtService.createRefreshToken();

        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getMemberId(), refreshToken);
    }
}
