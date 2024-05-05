package com.coverflow.question.domain;

import com.coverflow.company.domain.Company;
import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.member.domain.Member;
import com.coverflow.question.dto.request.SaveQuestionRequest;
import com.coverflow.question.dto.request.UpdateQuestionRequest;
import com.coverflow.report.domain.Report;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "tbl_question")
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 질문 고유 번호
    @Column
    private String title; // 제목
    @Column
    private String content; // 내용
    @Column
    private long viewCount; // 질문 조회 수
    @Column
    private int answerCount; // 답변 수
    @Column
    private int reward; // 채택 시 보상
    @Column
    private String questionCategory; // 질문 카테고리
    @Column
    private boolean selectionStatus; // 채택 상태 (T: 채택o/F: 채택x)
    @Column
    private boolean questionStatus; // 질문 상태 (T: 등록/F: 삭제)

    @Enumerated(EnumType.STRING)
    private QuestionTag questionTag; // 질문 태그 (문화/급여/업무/커리어/워라밸)

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonBackReference
    private Company company; // 회사 정보

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member; // 질문 작성자 정보

    @Builder.Default
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Answer> answers = new ArrayList<>(); // 질문에 대한 답변 리스트

    @Builder.Default
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Report> reports = new ArrayList<>(); // 질문에 대한 신고 리스트

    public Question(final UpdateQuestionRequest request) {
        this.title = request.title();
        this.content = request.content();
    }

    public Question(
            final SaveQuestionRequest request,
            final String memberId
    ) {
        this.title = request.title();
        this.content = request.content();
        this.viewCount = 1;
        this.answerCount = 0;
        this.reward = request.reward();
        this.questionTag = QuestionTag.valueOf(request.questionTag());
        this.questionStatus = true;
        this.company = Company.builder()
                .id(request.companyId())
                .build();
        this.member = Member.builder()
                .id(UUID.fromString(memberId))
                .build();
    }

    public void updateQuestion(final UpdateQuestionRequest request) {
        this.title = request.title();
        this.content = request.content();
        this.questionStatus = request.questionStatus();
    }

    public void updateViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public void updateAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public void updateSelectionStatus(boolean selectionStatus) {
        this.selectionStatus = selectionStatus;
    }
}
