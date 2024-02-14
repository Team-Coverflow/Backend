package com.coverflow.question.application;

import com.coverflow.company.domain.Company;
import com.coverflow.company.exception.CompanyException;
import com.coverflow.company.infrastructure.CompanyRepository;
import com.coverflow.member.application.CurrencyService;
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

    private final CurrencyService currencyService;
    private final CompanyRepository companyRepository;
    private final QuestionRepository questionRepository;

    /**
     * [특정 회사의 전체 질문 조회 메서드]
     * 회사 id로 조회
     */
    public List<QuestionResponse> findAllQuestionsByCompanyId(final Long companyId) {
        final List<Question> questions = questionRepository.findAllQuestionsByCompanyIdAndStatus(companyId, "등록")
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(companyId));
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
    @Transactional
    public FindQuestionResponse findQuestionById(final Long questionId) {
        final Question question = questionRepository.findByIdWithAnswers(questionId)
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(questionId));

        question.updateViewCount(question.getViewCount() + 1);
        return FindQuestionResponse.from(question);
    }

    /**
     * [관리자 전용: 전체 질문 조회 메서드]
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
    @Transactional
    public void saveQuestion(
            final SaveQuestionRequest request,
            final String memberId
    ) {
        final Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(request.companyId()));
        final Question question = Question.builder()
                .title(request.title())
                .content(request.content())
                .viewCount(1)
                .answerCount(0)
                .status("등록")
                .company(Company.builder()
                        .id(request.companyId())
                        .build())
                .member(Member.builder()
                        .id(UUID.fromString(memberId))
                        .build())
                .build();

        questionRepository.save(question);
        currencyService.writeQuestion(memberId);
        company.updateQuestionCount(company.getQuestionCount() + 1);
    }

    /**
     * [관리자 전용: 질문 수정 메서드]
     */
    @Transactional
    public void updateQuestion(final UpdateQuestionRequest request) {
        final Question question = questionRepository.findById(request.questionId())
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(request.questionId()));

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
