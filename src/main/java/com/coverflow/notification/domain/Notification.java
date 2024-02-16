package com.coverflow.notification.domain;

import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_notification")
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 알림 고유 번호
    @Column
    private String content; // 내용 (질문 고유 번호, 답변 고유 번호 등)
    @Column
    private boolean status; // 상태 (True/False)

    @Enumerated(EnumType.STRING)
    private NotificationType type; // 알림 종류 (DAILY, QUESTION, ANSWER)


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 회원 정보
}
