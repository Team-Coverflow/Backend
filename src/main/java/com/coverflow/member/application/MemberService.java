package com.coverflow.member.application;

import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.MemberRepository;
import com.coverflow.member.dto.request.MemberSaveMemberInfoRequest;
import com.coverflow.member.dto.request.MemberVerifyDuplicationNicknameRequest;
import com.coverflow.member.dto.response.MemberVerifyDuplicationNicknameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional(readOnly = true)
    public MemberVerifyDuplicationNicknameResponse verifyDuplicationNickname(
            final MemberVerifyDuplicationNicknameRequest request
    ) {
        final AtomicBoolean result = new AtomicBoolean(false);

        memberRepository.findByNickname(request.nickname())
                .ifPresentOrElse(
                        member -> result.set(false),
                        () -> result.set(true)
                );

//        if (nickname.isPresent()) {
//            return new DuplicationNicknameResponse(HttpStatus.OK, HttpStatus.OK.toString(), "이미 존재하는 닉네임입니다.");
//        }
//        return new DuplicationNicknameResponse(HttpStatus.OK, HttpStatus.OK.toString(), "사용 가능한 닉네임입니다.");
        return MemberVerifyDuplicationNicknameResponse.of(result.get());
    }

    public void saveMemberInfo(
            final String username,
            final MemberSaveMemberInfoRequest request
    ) {
        final Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

        member.saveMemberInfo(request);
    }
}
