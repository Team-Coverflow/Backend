package com.coverflow.board.domain;

import com.coverflow.company.domain.Company;
import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

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
    private int count; // 조회 수
    @Column
    private String status; // 상태

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateQuestion(final Question question) {
        this.title = question.getTitle();
        this.content = question.getContent();
    }

    public void updateStatus(final String status) {
        this.status = status;
    }
}
