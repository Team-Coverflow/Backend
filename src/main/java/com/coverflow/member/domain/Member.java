package com.coverflow.member.domain;

import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.inquiry.domain.Inquiry;
import com.coverflow.member.dto.MemberSignUpDTO;
import com.coverflow.member.dto.request.SaveMemberRequest;
import com.coverflow.member.dto.request.UpdateMemberRequest;
import com.coverflow.notification.domain.Notification;
import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.Question;
import com.coverflow.report.domain.Report;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_member")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // 회원 고유 번호
    @Column
    private String password; // 비밀번호
    @Column
    private String email; // 이메일
    @Column
    private String nickname; // 닉네임
    @Column
    private String tag; // 태그
    @Column
    private String age; // 연령대
    @Column
    private String gender; // 성별
    @Column
    private int fishShapedBun; // 붕어빵
    @Column
    private LocalDateTime connectedAt; // 마지막 접속 시간
    @Column
    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)
    @Column
    private String socialAccessToken; // 소셜 액세스 토큰
    @Column
    private String refreshToken; // 리프레쉬 토큰
    @Column
    private Boolean agreeMarketing; // 마케팅 광고 동의 여부
    @Column
    private Boolean agreeCollection; // 개인정보 수집 동의 여부

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    @Enumerated(EnumType.STRING)
    private Role role; // 권한

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus; // 회원 상태 (대기/등록/탈퇴)

    @Enumerated(EnumType.STRING)
    private RefreshTokenStatus refreshTokenStatus; // 리프레쉬 토큰 상태 (로그인/로그아웃)

    @Builder.Default
    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private List<Question> questions = new ArrayList<>(); // 회원의 질문 리스트

    @Builder.Default
    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private List<Answer> answers = new ArrayList<>(); // 회원의 답변 리스트

    @Builder.Default
    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private List<Notification> notifications = new ArrayList<>(); // 회원의 알림 리스트

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Inquiry> inquiries = new ArrayList<>(); // 회원의 문의 리스트

    @Builder.Default
    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private List<Report> reports = new ArrayList<>(); // 회원의 신고 리스트

    public Member(final MemberSignUpDTO memberSignUpDTO) {
        this.email = memberSignUpDTO.getEmail();
        this.nickname = memberSignUpDTO.getNickname();
        this.tag = memberSignUpDTO.getTag();
        this.age = memberSignUpDTO.getAge();
        this.gender = memberSignUpDTO.getGender();
        this.fishShapedBun = 300;
        this.socialId = memberSignUpDTO.getSocialId();
        this.socialType = memberSignUpDTO.getSocialType();
        this.role = Role.GUEST;
        this.memberStatus = MemberStatus.WAIT;
    }

    public void updateMember(final SaveMemberRequest request) {
        this.tag = request.tag();
        this.age = request.age();
        this.gender = request.gender();
    }

    public void updateMember(final UpdateMemberRequest request) {
        this.tag = request.tag();
        this.age = request.age();
        this.gender = request.gender();
    }

    public void updateRefreshToken(final String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public void updateNickname(final String updateNickname) {
        this.nickname = updateNickname;
    }

    public void updateConnectedAt() {
        this.connectedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    public void updateAuthorization(final Role role) {
        this.role = role;
    }

    public void updateFishShapedBun(final int fishShapedBun) {
        this.fishShapedBun = fishShapedBun;
    }

    public void updateSocialAccessToken(final String socialAccessToken) {
        this.socialAccessToken = socialAccessToken;
    }

    public void updateMemberStatus(final MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }

    public void updateTokenStatus(final RefreshTokenStatus refreshTokenStatus) {
        this.refreshTokenStatus = refreshTokenStatus;
    }

    public void updateAgreeMarketing(final boolean agreeMarketing) {
        this.agreeMarketing = agreeMarketing;
    }

    public void updateAgreeCollection(final boolean agreeCollection) {
        this.agreeCollection = agreeCollection;
    }

//    public void passwordEncode(final PasswordEncoder passwordEncoder) {
//        this.password = passwordEncoder.encode(this.password);
//    }
//
//    public void updatePassword(
//            final String updatePassword,
//            final PasswordEncoder passwordEncoder
//    ) {
//        this.password = passwordEncoder.encode(updatePassword);
//    }

}
