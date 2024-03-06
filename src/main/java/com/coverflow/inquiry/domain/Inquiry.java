package com.coverflow.inquiry.domain;

import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.inquiry.dto.request.SaveInquiryRequest;
import com.coverflow.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_inquiry")
public class Inquiry extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 문의 고유 번호
    @Column
    private String title; // 문의 제목
    @Column
    private String content; // 문의 내용
    @Column
    private String answer; // 관리자 답변

    @Enumerated(EnumType.STRING)
    private InquiryStatus inquiryStatus; // 상태 (답변대기/답변완료/삭제)

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 작성자 정보

    public Inquiry(
            final SaveInquiryRequest request,
            final String memberId
    ) {
        this.title = request.title();
        this.content = request.content();
        this.inquiryStatus = InquiryStatus.WAIT;
        this.member = Member.builder()
                .id(UUID.fromString(memberId))
                .build();
    }

    public void updateAnswer(final String answer) {
        this.answer = answer;
    }

    public void updateInquiryStatus(final InquiryStatus inquiryStatus) {
        this.inquiryStatus = inquiryStatus;
    }
}
