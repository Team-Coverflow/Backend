package com.coverflow.question.domain;

import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.member.domain.Member;
import com.coverflow.question.dto.request.SaveAnswerRequest;
import com.coverflow.question.dto.request.UpdateAnswerRequest;
import com.coverflow.report.domain.Report;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_answer")
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 답변 고유 번호
    @Column
    private String content; // 내용
    @Column
    private boolean selection; // 채택(T/F)

    @Enumerated(EnumType.STRING)
    private AnswerStatus answerStatus; // 답변 상태 (등록/삭제)

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question; // 질문 정보

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 답변 작성자 정보

    @Builder.Default
    @OneToMany(mappedBy = "answer", fetch = FetchType.LAZY)
    private List<Report> reports = new ArrayList<>(); // 답변에 대한 신고 리스트

    public Answer(
            final SaveAnswerRequest request,
            final String memberId
    ) {
        this.content = request.content();
        this.selection = false;
        this.answerStatus = AnswerStatus.REGISTRATION;
        this.question = Question.builder()
                .id(request.questionId())
                .build();
        this.member = Member.builder()
                .id(UUID.fromString(memberId))
                .build();
    }

    public void updateAnswer(final UpdateAnswerRequest request) {
        this.content = request.content();
        this.answerStatus = AnswerStatus.valueOf(request.answerStatus());
    }

    public void updateSelection(final boolean selection) {
        this.selection = selection;
    }

    public void updateAnswerStatus(final AnswerStatus answerStatus) {
        this.answerStatus = answerStatus;
    }
}
