package com.coverflow.report.domain;

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
@Table(name = "tbl_report")
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 신고 고유 번호
    @Column
    private String content; // 내용
    @Column
    private ReportType type; // 신고 종류
    @Column
    private String status; // 상태

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 작성자 정보

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question; // 질문 정보

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer; // 답변 정보

    public void updateStatus(final String status) {
        this.status = status;
    }
}
