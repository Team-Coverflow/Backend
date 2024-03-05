package com.coverflow.member.application;

import com.coverflow.global.util.NicknameUtil;
import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.MemberStatus;
import com.coverflow.member.domain.RefreshTokenStatus;
import com.coverflow.member.domain.Role;
import com.coverflow.member.dto.request.SaveMemberInfoRequest;
import com.coverflow.member.dto.response.FindAllMembersResponse;
import com.coverflow.member.dto.response.FindMemberInfoResponse;
import com.coverflow.member.dto.response.UpdateNicknameResponse;
import com.coverflow.member.infrastructure.MemberRepository;
import com.coverflow.notification.infrastructure.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.member.exception.MemberException.*;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmitterRepository emitterRepository;
    private final NicknameUtil nicknameUtil;

//    private final PasswordEncoder passwordEncoder;
//
//    /**
//     * 폼 로그인 구현 시 사용할 예정
//     */
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
    @Transactional(readOnly = true)
    public FindMemberInfoResponse findMemberById(final String username) {
        Member member = memberRepository.findByIdAndMemberStatus(UUID.fromString(username), MemberStatus.REGISTRATION)
                .orElseThrow(() -> new MemberNotFoundException(username));
        return FindMemberInfoResponse.from(member);
    }

    /**
     * [관리자 전용: 전체 회원 조회 메서드]
     */
    @Transactional(readOnly = true)
    public List<FindAllMembersResponse> findAllMembers(
            final int pageNo,
            final String criterion
    ) {
        Pageable pageable = PageRequest.of(pageNo, LARGE_PAGE_SIZE, Sort.by(criterion).descending());
        Page<Member> members = memberRepository.findAllMembers(pageable)
                .orElseThrow(AllMemberNotFoundException::new);

        return members.getContent().stream()
                .map(FindAllMembersResponse::from)
                .toList();
    }

    /**
     * [관리자 전용: 특정 상태 회원 조회 메서드]
     * 특정 상태(등록/탈퇴)의 회사를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public List<FindAllMembersResponse> findMembersByStatus(
            final int pageNo,
            final String criterion,
            final MemberStatus memberStatus
    ) {
        Pageable pageable = PageRequest.of(pageNo, LARGE_PAGE_SIZE, Sort.by(criterion).descending());
        Page<Member> members = memberRepository.findAllByMemberStatus(pageable, memberStatus)
                .orElseThrow(() -> new MemberNotFoundException(memberStatus));

        return members.getContent().stream()
                .map(FindAllMembersResponse::from)
                .toList();
    }

    /**
     * [회원 추가 정보 등록 메서드]
     */
    @Transactional
    public void saveMemberInfo(
            final String username,
            final SaveMemberInfoRequest request
    ) {
        Member member = memberRepository.findByIdAndMemberStatus(UUID.fromString(username), MemberStatus.REGISTRATION)
                .orElseThrow(() -> new MemberNotFoundException(username));

        member.saveMemberInfo(request);
        member.updateAuthorization(Role.MEMBER);
    }

    /**
     * [닉네임 변경 메서드]
     */
    @Transactional
    public UpdateNicknameResponse updateNickname(final String username) {
        Member member = memberRepository.findByIdAndMemberStatus(UUID.fromString(username), MemberStatus.REGISTRATION)
                .orElseThrow(() -> new MemberNotFoundException(username));
        String nickname = nicknameUtil.generateRandomNickname();

        if (member.getFishShapedBun() < 20) {
            throw new NotEnoughCurrencyException();
        }
        member.updateNickname(nickname);
        member.updateFishShapedBun(member.getFishShapedBun() - 20);
        return UpdateNicknameResponse.from(nickname);
    }

    /**
     * [로그아웃 메서드]
     */
    @Transactional
    public void logout(final String username) {
        Member member = memberRepository.findByIdAndMemberStatus(UUID.fromString(username), MemberStatus.REGISTRATION)
                .orElseThrow(() -> new MemberNotFoundException(username));

        member.updateTokenStatus(RefreshTokenStatus.LOGOUT);
        emitterRepository.deleteAllStartWithId(username);
        emitterRepository.deleteAllEventCacheStartWithId(username);
    }

    /**
     * [회원 탈퇴 메서드]
     */
    @Transactional
    public void leaveMember(final String username) {
        Member member = memberRepository.findByIdAndMemberStatus(UUID.fromString(username), MemberStatus.REGISTRATION)
                .orElseThrow(() -> new MemberNotFoundException(username));

        member.updateFishShapedBun(0);
        member.updateAuthorization(Role.GUEST);
        member.updateTokenStatus(RefreshTokenStatus.LOGOUT);
        member.updateStatus(MemberStatus.LEAVE);
        emitterRepository.deleteAllStartWithId(username);
        emitterRepository.deleteAllEventCacheStartWithId(username);
    }
}

