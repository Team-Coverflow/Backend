package com.coverflow.question.domain;

import com.coverflow.company.domain.Company;
import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_question")
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 질문 글 고유 번호
    @Column
    private String title; // 제목
    @Column
    private String content; // 내용
    @Column
    private int viewCount; // 질문 조회 수
    @Column
    private int answerCount; // 답변 개수
    @Column
    private String status; // 상태 (등록/삭제)

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company; // 회사 정보

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 질문 작성자 정보

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>(); // 질문에 대한 답변 리스트

    public void updateQuestion(final Question question) {
        this.title = question.getTitle();
        this.content = question.getContent();
    }

    public void updateStatus(final String status) {
        this.status = status;
    }

    public void updateViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void updateAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }
}
