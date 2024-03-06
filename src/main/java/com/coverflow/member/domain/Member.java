package com.coverflow.member.domain;

import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.member.dto.MemberSignUpDTO;
import com.coverflow.member.dto.request.SaveMemberInfoRequest;
import com.coverflow.notification.domain.Notification;
import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.Question;
import com.coverflow.report.domain.Report;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
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
    private String refreshToken; // 리프레쉬 토큰

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    @Enumerated(EnumType.STRING)
    private Role role; // 권한

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus; // 회원 상태 (등록/탈퇴)

    @Enumerated(EnumType.STRING)
    private RefreshTokenStatus refreshTokenStatus; // 리프레쉬 토큰 상태 (로그인/로그아웃)

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>(); // 회원의 질문 리스트

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>(); // 회원의 답변 리스트

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>(); // 회원의 알림 리스트

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Report> reports = new ArrayList<>(); // 회원의 신고 리스트

    public Member(final UUID id) {
        this.id = id;
    }

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
        this.memberStatus = MemberStatus.REGISTRATION;
        this.refreshTokenStatus = RefreshTokenStatus.LOGIN;
    }

    public void saveMemberInfo(final SaveMemberInfoRequest request) {
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

    public void updateAge(final String updateAge) {
        this.age = updateAge;
    }

    public void updateConnectedAt() {
        this.connectedAt = LocalDateTime.now();
    }

    public void updateAuthorization(final Role role) {
        this.role = role;
    }

    public void updateFishShapedBun(final int fishShapedBun) {
        this.fishShapedBun = fishShapedBun;
    }

    public void updateMemberStatus(final MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }

    public void updateTokenStatus(final RefreshTokenStatus refreshTokenStatus) {
        this.refreshTokenStatus = refreshTokenStatus;
    }

    public void passwordEncode(final PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updatePassword(
            final String updatePassword,
            final PasswordEncoder passwordEncoder
    ) {
        this.password = passwordEncoder.encode(updatePassword);
    }

}
