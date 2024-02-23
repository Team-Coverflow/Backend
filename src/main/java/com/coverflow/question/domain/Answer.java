package com.coverflow.question.domain;

import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.member.domain.Member;
import com.coverflow.report.domain.Report;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @Column
    private String status; // 상태 (등록/삭제)

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question; // 질문 정보

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 답변 작성자 정보

    @Builder.Default
    @OneToMany(mappedBy = "answer", fetch = FetchType.LAZY)
    private List<Report> reports = new ArrayList<>(); // 답변에 대한 신고 리스트

    public void updateAnswer(final Answer answer) {
        this.content = answer.getContent();
    }

    public void updateSelection(final boolean selection) {
        this.selection = selection;
    }

    public void updateStatus(final String status) {
        this.status = status;
    }
}
