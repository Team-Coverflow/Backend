package com.coverflow.member.application;

import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.MemberRepository;
import com.coverflow.member.domain.Role;
import com.coverflow.member.dto.MemberSignUpDTO;
import com.coverflow.member.dto.request.DuplicationNicknameRequest;
import com.coverflow.member.dto.response.DuplicationNicknameResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(final MemberSignUpDTO memberSignUpDTO) throws Exception {
        if (memberRepository.findByEmail(memberSignUpDTO.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if (memberRepository.findByNickname(memberSignUpDTO.getNickname()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        Member member = Member.builder()
                .email(memberSignUpDTO.getEmail())
                .password(memberSignUpDTO.getPassword())
                .nickname(memberSignUpDTO.getNickname())
                .age(memberSignUpDTO.getAge())
                .role(Role.MEMBER)
                .build();

        member.passwordEncode(passwordEncoder);
        memberRepository.save(member);
    }

    public DuplicationNicknameResponse verifyDuplicationNickname(final DuplicationNicknameRequest request) {
        if (memberRepository.findByNickname(request.nickname()).isPresent()) {
            return new DuplicationNicknameResponse(HttpStatus.OK, HttpStatus.OK.toString(), "이미 존재하는 닉네임입니다.");
        }
        return new DuplicationNicknameResponse(HttpStatus.OK, HttpStatus.OK.toString(), "사용 가능한 닉네임입니다.");
    }
}
