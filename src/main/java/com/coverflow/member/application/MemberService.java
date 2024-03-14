package com.coverflow.member.application;

import com.coverflow.global.util.NicknameUtil;
import com.coverflow.inquiry.infrastructure.InquiryRepository;
import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.MemberStatus;
import com.coverflow.member.domain.RefreshTokenStatus;
import com.coverflow.member.domain.Role;
import com.coverflow.member.dto.request.SaveMemberRequest;
import com.coverflow.member.dto.response.FindAllMembersResponse;
import com.coverflow.member.dto.response.FindMemberInfoResponse;
import com.coverflow.member.dto.response.UpdateNicknameResponse;
import com.coverflow.member.infrastructure.MemberRepository;
import com.coverflow.notification.infrastructure.EmitterRepository;
import com.coverflow.notification.infrastructure.NotificationRepository;
import com.coverflow.question.infrastructure.AnswerRepository;
import com.coverflow.question.infrastructure.QuestionRepository;
import com.coverflow.report.infrastructure.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.global.util.PageUtil.generatePageDesc;
import static com.coverflow.member.exception.MemberException.*;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final InquiryRepository inquiryRepository;
    private final ReportRepository reportRepository;
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final NicknameUtil nicknameUtil;
    private final WebClient webClient;

    @Value("${kakao.admin-key}")
    private String adminKey;

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
        Page<Member> members = memberRepository.findAllMembers(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion))
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
        Page<Member> members = memberRepository.findAllByMemberStatus(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion), memberStatus)
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
            final SaveMemberRequest request
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
     * 30일 동안 유예 상태 및 GUEST 권한으로 전환(모든 데이터 보존)
     * 30일 이후에 탈퇴(모든 데이터 소멸)
     */
    @Transactional
    public void suspend(final String username) {
        Member member = memberRepository.findByIdAndMemberStatus(UUID.fromString(username), MemberStatus.REGISTRATION)
                .orElseThrow(() -> new MemberNotFoundException(username));

        member.updateAuthorization(Role.GUEST);
        member.updateMemberStatus(MemberStatus.LEAVE);
        member.updateTokenStatus(RefreshTokenStatus.LOGOUT);

        emitterRepository.deleteAllStartWithId(String.valueOf(member.getId()));
        emitterRepository.deleteAllEventCacheStartWithId(String.valueOf(member.getId()));
    }

    /**
     * [30일 후 유예 회원들 탈퇴로 진행하는 메서드]
     * 회원과 연관된 모든 엔티티의 인스턴스들을 지워야 한다.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void leave() {
        LocalDateTime date = LocalDateTime.now().minusDays(30);
        List<Member> members = memberRepository.findByStatus(date)
                .orElseThrow(MemberNotFoundException::new);

        // 회원이 작성한 질문, 답변, 문의, 신고, 알림 데이터 삭제
        for (Member member : members) {
            answerRepository.deleteByMemberId(member.getId());
            questionRepository.deleteByMemberId(member.getId());
            inquiryRepository.deleteByMemberId(member.getId());
            reportRepository.deleteByMemberId(member.getId());

            emitterRepository.deleteAllStartWithId(String.valueOf(member.getId()));
            emitterRepository.deleteAllEventCacheStartWithId(String.valueOf(member.getId()));
            notificationRepository.deleteByMemberId(member.getId());

            if (unlinkKakaoUser(adminKey, member.getSocialId()).block() == null) {
                throw new RuntimeException();
            }
        }

        // 탈퇴 회원 데이터 물리 삭제
        memberRepository.deleteMembersWithStatus(date);
    }

    /**
     * [카카오 연결 끊기]
     */
    private Mono<String> unlinkKakaoUser(
            final String adminKey,
            final String userId
    ) {
        String url = "https://kapi.kakao.com/v1/user/unlink";

        return webClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + adminKey)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("target_id_type=user_id&target_id=" + userId)
                .retrieve() // 실제 요청을 실행합니다.
                .bodyToMono(String.class); // 응답 바디를 String으로 변환합니다.
    }
}