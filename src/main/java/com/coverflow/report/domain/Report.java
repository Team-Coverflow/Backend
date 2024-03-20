package com.coverflow.report.domain;

import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.member.domain.Member;
import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.Question;
import com.coverflow.report.dto.request.SaveReportRequest;
import com.coverflow.report.dto.request.UpdateReportRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus; // 상태(등록/삭제)

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 작성자 정보

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question; // 질문 정보

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer; // 답변 정보

    public Report(
            final SaveReportRequest request,
            final String memberId
    ) {
        this.content = request.content();
        this.reportStatus = ReportStatus.REGISTRATION;
        this.member = Member.builder()
                .id(UUID.fromString(memberId))
                .build();
        this.question = Question.builder()
                .id(request.id())
                .build();
    }

    public Report(
            final SaveReportRequest request,
            final Answer answer,
            final String memberId
    ) {
        this.content = request.content();
        this.reportStatus = ReportStatus.REGISTRATION;
        this.member = Member.builder()
                .id(UUID.fromString(memberId))
                .build();
        this.question = Question.builder()
                .id(answer.getId())
                .build();
        this.answer = Answer.builder()
                .id(request.id())
                .build();
    }

    public void updateReport(final UpdateReportRequest request) {
        this.reportStatus = ReportStatus.valueOf(request.updateStatus());
    }
}
