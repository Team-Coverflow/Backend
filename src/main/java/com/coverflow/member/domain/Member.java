package com.coverflow.member.domain;

import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.member.dto.request.SaveMemberInfoRequest;
import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.Question;
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
    private String status; // 상태 (등록/탈퇴)
    @Column
    private LocalDateTime connected_at;
    @Column
    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)
    @Column
    private String refreshToken; // 리프레쉬 토큰
    @Column
    private String tokenStatus; // 리프레쉬 토큰 상태 (로그인/로그아웃)

    @Enumerated(EnumType.STRING)
    private Role role; // 권한

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>(); // 회원의 질문 리스트

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>(); // 회원의 답변 리스트

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
        this.connected_at = LocalDateTime.now();
    }

    public void updateAuthorization(final Role role) {
        this.role = role;
    }

    public void updateStatus(final String updateStatus) {
        this.status = updateStatus;
    }

    public void updateTokenStatus(final String updateTokenStatus) {
        this.tokenStatus = updateTokenStatus;
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
