package com.coverflow.global.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.coverflow.global.exception.GlobalException;
import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.RefreshTokenStatus;
import com.coverflow.member.domain.Role;
import com.coverflow.member.exception.MemberException;
import com.coverflow.member.infrastructure.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Getter
@Service
public class JwtService {
    /**
     * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정
     * JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
     */
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String MEMBER_ID_CLAIM = "memberId";
    private static final String ROLE = "role";
    private static final String BEARER = "Bearer ";
    private final MemberRepository memberRepository;
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private long accessTokenExpirationPeriod;
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpirationPeriod;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(
            final String memberId,
            final Role role
    ) {
        Date now = new Date();
        return JWT.create() // JWT 토큰을 생성하는 빌더 반환
                .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정

                // 클레임으로는 memberId, role 사용
                // 추가적으로 식별자나, 이름 등의 정보를 더 추가 가능
                // .withClaim(클래임 이름, 클래임 값)로 추가
                .withClaim(MEMBER_ID_CLAIM, memberId)
                .withClaim(ROLE, role.toString())

                // HMAC512 알고리즘 사용
                // application.yml에서 지정한 secretKey로 암호화
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * RefreshToken 생성
     * RefreshToken은 Claim에 회원 정보 넣지 않으므로 withClaim() X
     */
    public String createRefreshToken(
    ) {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * AccessToken 헤더에 실어서 보내기
     */
    public void sendAccessToken(
            final HttpServletResponse response, final String accessToken
    ) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }

    /**
     * AccessToken + RefreshToken 헤더에 실어서 보내기
     */
    public void sendAccessAndRefreshToken(
            final HttpServletResponse response,
            final String accessToken,
            final String refreshToken
    ) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    /**
     * 헤더에서 AccessToken 추출
     * 토큰 형식: Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractAccessToken(
            final HttpServletRequest request
    ) {
        return Optional.ofNullable(Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""))
                .orElseThrow(GlobalException.JWTNotFoundException::new));
    }

    /**
     * 헤더에서 RefreshToken 추출
     * 토큰 형식: Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractRefreshToken(
            final HttpServletRequest request
    ) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * AccessToken에서 회원 ID 추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 회원 ID 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<UUID> extractMemberId(
            final String accessToken
    ) {
        // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
        UUID memberId = JWT.require(Algorithm.HMAC512(secretKey))
                .build() // 반환된 빌더로 JWT verifier 생성
                .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                .getClaim(MEMBER_ID_CLAIM) // claim(MemberId) 가져오기
                .as(UUID.class);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberException.MemberNotFoundException::new);

        if ((RefreshTokenStatus.LOGOUT).equals(member.getRefreshTokenStatus())) {
            throw new GlobalException.LogoutMemberException();
        }

        return Optional.of(memberId);
    }

    /**
     * AccessToken 헤더 설정
     */
    public void setAccessTokenHeader(
            final HttpServletResponse response,
            final String accessToken
    ) {
        response.setHeader(accessHeader, accessToken);
    }

    /**
     * RefreshToken 헤더 설정
     */
    public void setRefreshTokenHeader(
            final HttpServletResponse response,
            final String refreshToken
    ) {
        response.setHeader(refreshHeader, refreshToken);
    }

    /*
      RefreshToken DB 저장(업데이트)
      사용 x
     */
//    @Transactional
//    public void updateRefreshToken(
//            final UUID memberId,
//            final String refreshToken
//    ) {
//        Member member = memberRepository.findByIdAndMemberStatus(memberId, MemberStatus.REGISTRATION)
//                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));
//
//        member.updateRefreshToken(refreshToken);
//    }

    /**
     * [토큰 유효성 검사 메서드]
     */
    public boolean isTokenValid(
            final String token
    ) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("액세스 토큰이 유효하지 않습니다.");
        }

        return true;
    }
}
