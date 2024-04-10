package com.coverflow.notification.domain;

import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.inquiry.domain.Inquiry;
import com.coverflow.member.domain.Member;
import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.Question;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private String uri; // 필요 시 리다이렉트 시킬 uri
    @Column
    private boolean isRead; // 상태 (T: 읽음, F: 안 읽음)

    @Enumerated(EnumType.STRING)
    private NotificationType type; // 알림 종류 (DAILY, QUESTION, ANSWER)

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member; // 회원 정보

    public Notification(final Member member) {
        this.type = NotificationType.DAILY;
        this.isRead = false;
        this.member = member;
    }

    public Notification(final Question question) {
        this.content = question.getCompany().getName();
        this.uri = "/company-info/" + question.getCompany().getId().toString() + "/" + question.getId().toString();
        this.type = NotificationType.ANSWER;
        this.isRead = false;
        this.member = question.getMember();
    }

    public Notification(final Answer answer) {
        this.content = answer.getQuestion().getCompany().getName();
        this.uri = "/company-info/" + answer.getQuestion().getCompany().getId().toString() + "/" + answer.getQuestion().getId().toString();
        this.type = NotificationType.SELECTION;
        this.isRead = false;
        this.member = answer.getMember();
    }

    public Notification(final Inquiry inquiry) {
        this.uri = "contact" + inquiry.getId().toString();
        this.type = NotificationType.INQUIRY;
        this.isRead = false;
        this.member = inquiry.getMember();
    }

    public void updateIsRead(final boolean isRead) {
        this.isRead = isRead;
    }
}
