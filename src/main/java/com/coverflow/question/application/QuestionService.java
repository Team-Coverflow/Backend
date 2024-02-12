package com.coverflow.question.application;

import com.coverflow.company.domain.Company;
import com.coverflow.member.domain.Member;
import com.coverflow.question.domain.Question;
import com.coverflow.question.dto.request.SaveQuestionRequest;
import com.coverflow.question.dto.request.UpdateQuestionRequest;
import com.coverflow.question.dto.response.FindQuestionResponse;
import com.coverflow.question.dto.response.QuestionResponse;
import com.coverflow.question.exception.QuestionException;
import com.coverflow.question.infrastructure.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    /**
     * [특정 회사의 전체 질문 조회 메서드]
     * 회사 id로 조회
     */
    public List<QuestionResponse> findAllQuestionsByCompanyId(final Long id) {
        final List<Question> questions = questionRepository.findAllQuestionsByCompanyIdAndStatus(id, "등록")
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(id));
        final List<QuestionResponse> findQuestions = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            findQuestions.add(i, QuestionResponse.from(questions.get(i)));
        }
        return findQuestions;
    }

    /**
     * [특정 질문 조회 메서드]
     * 특정 질문 id로 질문 및 답변 리스트 조회
     */
    public FindQuestionResponse findQuestionById(final Long id) {
        final Question question = questionRepository.findByIdWithAnswers(id)
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(id));
        return FindQuestionResponse.from(question);
    }

    /**
     * [관리자 전용: 전체 질문 조회 메서드]
     * 회사 id로 조회
     */
    public List<QuestionResponse> findAllQuestions() {
        final List<Question> questions = questionRepository.findAllQuestions()
                .orElseThrow(QuestionException.QuestionNotFoundException::new);
        final List<QuestionResponse> findQuestions = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            findQuestions.add(i, QuestionResponse.from(questions.get(i)));
        }
        return findQuestions;
    }

    /**
     * [질문 등록 메서드]
     */
    public void saveQuestion(
            final SaveQuestionRequest request,
            final String memberId
    ) {
        final Question question = Question.builder()
                .title(request.title())
                .content(request.content())
                .count(1)
                .status("등록")
                .company(Company.builder()
                        .id(request.companyId())
                        .build())
                .member(Member.builder()
                        .id(UUID.fromString(memberId))
                        .build())
                .build();

        questionRepository.save(question);
    }

    /**
     * [관리자 전용: 질문 수정 메서드]
     */
    @Transactional
    public void updateQuestion(final UpdateQuestionRequest request) {
        final Question question = questionRepository.findById(request.id())
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(request.id()));

        question.updateQuestion(Question.builder()
                .title(request.title())
                .content(request.content())
                .build());
    }

    /**
     * [관리자 전용: 질문 삭제 메서드]
     */
    @Transactional
    public void deleteQuestion(final Long questionId) {
        final Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(questionId));

        question.updateStatus("삭제");
    }
}
