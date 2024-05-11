package com.coverflow.member.application;

import com.coverflow.global.util.NicknameUtil;
import com.coverflow.inquiry.infrastructure.InquiryRepository;
import com.coverflow.member.domain.*;
import com.coverflow.member.dto.MembersDTO;
import com.coverflow.member.dto.request.FindMemberAdminRequest;
import com.coverflow.member.dto.request.SaveMemberRequest;
import com.coverflow.member.dto.request.UpdateMemberRequest;
import com.coverflow.member.dto.response.FindAllMembersResponse;
import com.coverflow.member.dto.response.FindMemberResponse;
import com.coverflow.member.dto.response.UpdateNicknameResponse;
import com.coverflow.member.exception.MemberException;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.global.util.PageUtil.generatePageDesc;
import static com.coverflow.member.exception.MemberException.MemberNotFoundException;
import static com.coverflow.member.exception.MemberException.NotEnoughCurrencyException;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final InquiryRepository inquiryRepository;
    private final ReportRepository reportRepository;
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final NicknameUtil nicknameUtil;
    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

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

    @Override
    @Transactional(readOnly = true)
    public FindMemberResponse findMyMember(final String memberId) {
        Member member = memberRepository.findByIdAndMemberStatus(UUID.fromString(memberId), MemberStatus.REGISTRATION)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        return FindMemberResponse.from(member);
    }

    @Override
    @Transactional(readOnly = true)
    public FindAllMembersResponse find(
            final int pageNo,
            final String criterion,
            final FindMemberAdminRequest request
    ) {
        Page<Member> members = memberRepository.findWithFilters(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion), request)
                .orElseThrow(() -> new MemberNotFoundException(request));

        return FindAllMembersResponse.of(
                members.getTotalPages(),
                members.getTotalElements(),
                members.getContent()
                        .stream()
                        .map(MembersDTO::from)
                        .toList()
        );
    }

    @Override
    @Transactional
    public void save(
            final String memberId,
            final SaveMemberRequest request
    ) {
        Member member = memberRepository.findByIdAndMemberStatus(UUID.fromString(memberId), MemberStatus.REGISTRATION)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        member.updateMember(request);
    }

    @Override
    @Transactional
    public void update(
            final String memberId,
            final UpdateMemberRequest request
    ) {
        Member member = memberRepository.findByIdAndMemberStatus(UUID.fromString(memberId), MemberStatus.REGISTRATION)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        member.updateMember(request);
    }

    @Override
    @Transactional
    public UpdateNicknameResponse updateNickname(final String memberId) {
        Member member = memberRepository.findByIdAndMemberStatus(UUID.fromString(memberId), MemberStatus.REGISTRATION)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        String nickname = nicknameUtil.generateRandomNickname();

        if (member.getFishShapedBun() < 20) {
            throw new NotEnoughCurrencyException();
        }
        member.updateNickname(nickname);
        member.updateFishShapedBun(member.getFishShapedBun() - 20);
        return UpdateNicknameResponse.from(nickname);
    }

    @Override
    @Transactional
    public void writeQuestion(
            final String memberId,
            final int currency
    ) {
        Member member = memberRepository.findById(UUID.fromString(memberId))
                .orElseThrow(() -> new MemberException.MemberNotFoundException(memberId));

        if (member.getFishShapedBun() >= 10 + currency) {
            member.updateFishShapedBun(member.getFishShapedBun() - 10 - currency);
            return;
        }
        throw new MemberException.NotEnoughCurrencyException();
    }

    @Override
    @Transactional
    public void logout(final String memberId) {
        Member member = memberRepository.findByIdAndMemberStatus(UUID.fromString(memberId), MemberStatus.REGISTRATION)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        member.updateTokenStatus(RefreshTokenStatus.LOGOUT);
        emitterRepository.deleteAllStartWithId(memberId);
        emitterRepository.deleteAllEventCacheStartWithId(memberId);
    }

    @Override
    @Transactional
    public void delete(final String memberId) {
        Member member = memberRepository.findByIdAndMemberStatus(UUID.fromString(memberId), MemberStatus.REGISTRATION)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        member.updateAuthorization(Role.GUEST);
        member.updateMemberStatus(MemberStatus.LEAVE);
        member.updateTokenStatus(RefreshTokenStatus.LOGOUT);

        emitterRepository.deleteAllStartWithId(String.valueOf(member.getId()));
        emitterRepository.deleteAllEventCacheStartWithId(String.valueOf(member.getId()));
    }

    /**
     * [30일 후 유예 회원들 탈퇴로 진행하는 메서드]
     * 매일 자정에
     * 유예 기간이 30일이 지난 회원들의 데이터를 삭제합니다.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void leave() {
        LocalDateTime date = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().minusDays(30);
        List<Member> members = memberRepository.findByStatus(date)
                .orElseThrow(MemberNotFoundException::new);

        for (Member member : members) {
            // 회원이 작성한 질문, 답변, 문의, 신고, 알림 데이터 삭제
            deleteData(member.getId());

            // 소셜 연결 끊기
            unlink(member.getSocialType(), member.getSocialId(), member.getSocialAccessToken());
        }

        // 탈퇴 회원 데이터 물리 삭제
        memberRepository.deleteMembersWithStatus(date);
    }

    /**
     * [탈퇴에 따른 데이터 삭제 메서드]
     */
    private void deleteData(final UUID memberId) {
        reportRepository.deleteByMemberId(memberId);
        answerRepository.deleteByMemberId(memberId);
        questionRepository.deleteByMemberId(memberId);
        inquiryRepository.deleteByMemberId(memberId);

        emitterRepository.deleteAllStartWithId(String.valueOf(memberId));
        emitterRepository.deleteAllEventCacheStartWithId(String.valueOf(memberId));
        notificationRepository.deleteByMemberId(memberId);
    }

    /**
     * [연결 끊기 메서드]
     */
    private void unlink(
            final SocialType socialType,
            final String memberId,
            final String socialAccessToken) {
        if (("KAKAO").equals(String.valueOf(socialType))) {
            unlinkKakao(memberId, socialAccessToken).block();
        }
        if (("NAVER").equals(String.valueOf(socialType))) {
            unlinkNaver(socialAccessToken).block();
        }
        if (("GOOGLE").equals(String.valueOf(socialType))) {
            unlinkGoogle(socialAccessToken).block();
        }
        throw new RuntimeException();
    }

    /**
     * [카카오 연결 끊기]
     */
    private Mono<String> unlinkKakao(
            final String memberId,
            final String accessToken
    ) {
        String url = "https://kapi.kakao.com/v1/user/unlink";

        return webClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("target_id_type=user_id&target_id=" + memberId)
                .retrieve() // 실제 요청을 실행합니다.
                .bodyToMono(String.class); // 응답 바디를 String으로 변환합니다.
    }

    /**
     * [네이버 연결 끊기]
     */
    private Mono<String> unlinkNaver(final String accessToken) {
        String url = "https://nid.naver.com/oauth2.0/token";

        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("grant_type", "delete")
                        .queryParam("client_id", naverClientId)
                        .queryParam("client_secret", naverClientSecret)
                        .queryParam("access_token", accessToken)
                        .queryParam("service_provider", "NAVER")
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    /**
     * [구글 연결 끊기]
     */
    private Mono<String> unlinkGoogle(final String accessToken) {
        String url = "https://accounts.google.com/o/oauth2/revoke";

        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("token", accessToken)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}