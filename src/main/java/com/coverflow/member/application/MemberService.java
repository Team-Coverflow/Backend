package com.coverflow.member.application;

import com.coverflow.global.util.NicknameUtil;
import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.Role;
import com.coverflow.member.dto.request.SaveMemberInfoRequest;
import com.coverflow.member.dto.response.FindMemberInfoResponse;
import com.coverflow.member.dto.response.UpdateNicknameResponse;
import com.coverflow.member.exception.MemberException;
import com.coverflow.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    //    private final PasswordEncoder passwordEncoder;
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

    /**
     * [특정 회원 조회 메서드]
     */
    public FindMemberInfoResponse findMemberById(String username) {
        final Member member = memberRepository.findByMemberIdAndStatus(UUID.fromString(username), "등록")
                .orElseThrow(() -> new MemberException.MemberNotFoundException(username));
        return FindMemberInfoResponse.of(member);
    }

    /**
     * [관리자 전용: 전체 회원 조회 메서드]
     */
    public List<FindMemberInfoResponse> findAllMembers() {
        final List<Member> members = memberRepository.findAllMembers()
                .orElseThrow(() -> new MemberException.AllMemberNotFoundException());
        final List<FindMemberInfoResponse> findMembers = new ArrayList<>();

        for (int i = 0; i < members.size(); i++) {
            findMembers.add(i, FindMemberInfoResponse.of(members.get(i)));
        }
        return findMembers;
    }

    /**
     * [회원 추가 정보 등록 메서드]
     */
    @Transactional
    public void saveMemberInfo(
            final String username,
            final SaveMemberInfoRequest request
    ) {
        final Member member = memberRepository.findByMemberIdAndStatus(UUID.fromString(username), "등록")
                .orElseThrow(() -> new MemberException.MemberNotFoundException(username));

        member.saveMemberInfo(request);
        member.updateAuthorization(Role.MEMBER);
    }

    /**
     * [닉네임 변경 메서드]
     */
    @Transactional
    public UpdateNicknameResponse updateNickname(final String username) {
        final Member member = memberRepository.findByMemberIdAndStatus(UUID.fromString(username), "등록")
                .orElseThrow(() -> new MemberException.MemberNotFoundException(username));
        final String nickname = nicknameUtil.generateRandomNickname();

        member.updateNickname(nickname);
        return UpdateNicknameResponse.of(nickname);
    }

    /**
     * [로그아웃 메서드]
     */
    @Transactional
    public void logout(final String username) {
        final Member member = memberRepository.findByMemberIdAndStatus(UUID.fromString(username), "등록")
                .orElseThrow(() -> new MemberException.MemberNotFoundException(username));

        member.updateTokenStatus("로그아웃");
    }

    /**
     * [회원 탈퇴 메서드]
     */
    @Transactional
    public void leaveMember(final String username) {
        final Member member = memberRepository.findByMemberIdAndStatus(UUID.fromString(username), "등록")
                .orElseThrow(() -> new MemberException.MemberNotFoundException(username));

        member.updateTokenStatus("로그아웃");
        member.updateAuthorization(Role.GUEST);
        member.updateStatus("탈퇴");
    }
}

