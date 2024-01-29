package com.coverflow.member.application;

import com.coverflow.global.util.NicknameUtil;
import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.Role;
import com.coverflow.member.dto.request.MemberSaveMemberInfoRequest;
import com.coverflow.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final NicknameUtil nicknameUtil;

    /**
     * 폼 로그인 구현 시 사용할 예정
     */
    //    public void signUp(final MemberSignUpDTO memberSignUpDTO) throws Exception {
//        if (memberRepository.findByEmail(memberSignUpDTO.getEmail()).isPresent()) {
//            throw new Exception("이미 존재하는 이메일입니다.");
//        }
//
//        if (memberRepository.findByNickname(memberSignUpDTO.getNickname()).isPresent()) {
//            throw new Exception("이미 존재하는 닉네임입니다.");
//        }
//
//        final Member member = Member.builder()
//                .email(memberSignUpDTO.getEmail())
//                .password(memberSignUpDTO.getPassword())
//                .nickname(memberSignUpDTO.getNickname())
//                .age(memberSignUpDTO.getAge())
//                .role(Role.MEMBER)
//                .build();
//
//        member.passwordEncode(passwordEncoder);
//        memberRepository.save(member);
//    }
    @Transactional
    public void saveMemberInfo(
            final String username,
            final MemberSaveMemberInfoRequest request
    ) {
        final Member member = memberRepository.findByMemberId(UUID.fromString(username))
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

        member.saveMemberInfo(request);
        member.updateAuthorization(Role.MEMBER);
    }

    @Transactional
    public void updateNickname(
            final String username
    ) {
        final Member member = memberRepository.findByMemberId(UUID.fromString(username))
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));
        final String nickname = nicknameUtil.generateRandomNickname();

        member.updateNickname(nickname);
    }

    @Transactional
    public void logout(final String username) {
        final Member member = memberRepository.findByMemberId(UUID.fromString(username))
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

        member.updateTokenStatus("로그아웃");
    }

    @Transactional
    public void leaveMember(final String username) {
        final Member member = memberRepository.findByMemberId(UUID.fromString(username))
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

        member.updateTokenStatus("로그아웃");
        member.updateAuthorization(Role.GUEST);
        member.updateStatus("탈퇴");
    }
}

