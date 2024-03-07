package com.coverflow.notification.domain;

import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.member.domain.Member;
import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.Question;
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
    private String content; // 내용
    @Column
    private String url; // 필요 시 리다이렉트 시킬 url

    @Enumerated(EnumType.STRING)
    private NotificationType type; // 알림 종류 (DAILY, QUESTION, ANSWER)

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus; // 상태 (안읽음/읽음/삭제)

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 회원 정보

    public Notification(final Question question) {
        this.content = question.getCompany().getName();
        this.url = "/company-info/" +
                question.getCompany().getId().toString() +
                "/" +
                question.getId().toString();
        this.type = NotificationType.ANSWER;
        this.notificationStatus = NotificationStatus.NO;
        this.member = question.getMember();
    }

    public Notification(
            final Answer answer,
            final Member member
    ) {
        this.content = answer.getQuestion().getCompany().getName();
        this.url = "/company-info/" +
                answer.getQuestion().getCompany().getId().toString() +
                "/" +
                answer.getQuestion().getId().toString();
        this.type = NotificationType.SELECTION;
        this.notificationStatus = NotificationStatus.NO;
        this.member = member;
    }

    public void updateNotificationStatus(final NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
}
