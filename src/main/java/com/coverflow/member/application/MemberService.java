package com.coverflow.member.application;

import com.coverflow.member.domain.MemberRepository;
import com.coverflow.member.dto.request.DuplicationNicknameRequest;
import com.coverflow.member.dto.request.LoginInfoRequest;
import com.coverflow.member.dto.response.DuplicationNicknameResponse;
import com.coverflow.member.dto.response.LoginInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

//    public void signUp(
//            final MemberSignUpDTO memberSignUpDTO
//    ) throws Exception {
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
    public DuplicationNicknameResponse verifyDuplicationNickname(
            final DuplicationNicknameRequest request
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
        return DuplicationNicknameResponse.of(result.get());
    }

    public LoginInfoResponse saveLoginInfo(
            final LoginInfoRequest request
    ) {
        Optional<>
    }
}
