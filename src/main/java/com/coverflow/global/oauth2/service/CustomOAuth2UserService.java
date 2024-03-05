package com.coverflow.global.oauth2.service;

import com.coverflow.global.oauth2.CustomOAuth2User;
import com.coverflow.global.oauth2.OAuthAttributes;
import com.coverflow.global.util.NicknameUtil;
import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.SocialType;
import com.coverflow.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";
    private final MemberRepository memberRepository;
    private final NicknameUtil nicknameUtil;

    @Override
    @Transactional
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        /*
          DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
          DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
          사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
          결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /*
          userRequest에서 registrationId 추출 후 registrationId으로 SocialType 저장
          http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
          userNameAttributeName은 이후에 nameAttributeKey로 설정된다.
         */
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);

        // OAuth2 로그인 시 키(PK)가 되는 값
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        // getMember() 메소드로 Member 객체 생성 후 반환
        Member createdMember = getMember(extractAttributes, socialType);

        // DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성해서 반환
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdMember.getRole().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdMember.getId(),
                createdMember.getRole()
        );
    }

    private SocialType getSocialType(final String registrationId) {
        if (NAVER.equals(registrationId)) {
            return SocialType.NAVER;
        }
        if (KAKAO.equals(registrationId)) {
            return SocialType.KAKAO;
        }
        return SocialType.GOOGLE;
    }

    /**
     * SocialType과 attributes에 들어있는 소셜 로그인의 식별값 id를 통해 회원을 찾아 반환하는 메소드
     * 만약 회원이 존재하면 그대로 반환하고
     * 없거나 탈퇴한 회원이면 saveMember()를 호출하여 회원을 저장한다.
     */
    private Member getMember(
            final OAuthAttributes attributes,
            final SocialType socialType
    ) {
        Member findMember = memberRepository.findBySocialTypeAndSocialIdAndStatus(
                        socialType,
                        attributes.getOauth2UserInfo().getId(),
                        "등록"
                )
                .orElse(null);

        if (findMember == null || findMember.getStatus().equals("탈퇴")) {
            return saveMember(attributes, socialType);
        }
        return findMember;
    }

    /**
     * OAuthAttributes의 toEntity() 메소드를 통해 빌더로 Member 객체 생성 후 반환
     * 생성된 Member 객체를 DB에 저장
     */
    private Member saveMember(
            final OAuthAttributes attributes,
            final SocialType socialType
    ) {
        String nickname = nicknameUtil.generateRandomNickname();
        Member createdMember = attributes.toEntity(socialType, attributes.getOauth2UserInfo(), nickname);
        return memberRepository.save(createdMember);
    }
}
