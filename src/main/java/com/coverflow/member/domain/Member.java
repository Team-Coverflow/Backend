package com.coverflow.member.domain;

import com.coverflow.board.domain.Question;
import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.member.dto.request.SaveMemberInfoRequest;
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
    private UUID memberId;
    private String password;
    private String email;
    private String nickname;
    private String tag;
    private String age;
    private String gender;
    private int fishShapedBun;
    private String status;
    private LocalDateTime connected_at;
    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)
    private String refreshToken; // 리프레쉬 토큰
    private String tokenStatus; // 리프레쉬 토큰 상태

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    @OneToMany(mappedBy = "member")
    private List<Question> questions = new ArrayList<>();

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
