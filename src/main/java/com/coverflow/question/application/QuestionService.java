package com.coverflow.question.application;

import com.coverflow.company.domain.Company;
import com.coverflow.company.exception.CompanyException;
import com.coverflow.company.infrastructure.CompanyRepository;
import com.coverflow.member.application.CurrencyService;
import com.coverflow.question.domain.Question;
import com.coverflow.question.domain.QuestionStatus;
import com.coverflow.question.dto.QuestionDTO;
import com.coverflow.question.dto.QuestionListDTO;
import com.coverflow.question.dto.request.SaveQuestionRequest;
import com.coverflow.question.dto.request.UpdateQuestionRequest;
import com.coverflow.question.dto.response.FindAllQuestionsResponse;
import com.coverflow.question.dto.response.FindQuestionResponse;
import com.coverflow.question.exception.QuestionException;
import com.coverflow.question.infrastructure.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.global.constant.Constant.SMALL_PAGE_SIZE;
import static com.coverflow.global.util.PageUtil.generatePageDesc;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final CurrencyService currencyService;
    private final AnswerService answerService;
    private final CompanyRepository companyRepository;
    private final QuestionRepository questionRepository;

    /**
     * [특정 기업의 질문 조회 메서드]
     * 기업 id로 조회
     */
    @Transactional(readOnly = true)
    public QuestionListDTO findAllQuestionsByCompanyId(
            final int pageNo,
            final String criterion,
            final long companyId
    ) {
        Optional<Page<Question>> questionList = questionRepository.findRegisteredQuestions(generatePageDesc(pageNo, SMALL_PAGE_SIZE, criterion), companyId);

        return questionList
                .map(questionPage ->
                        new QuestionListDTO(questionPage.getTotalPages(), questionPage.getContent().stream().map(QuestionDTO::from).toList())
                )
                .orElseGet(() -> new QuestionListDTO(0, new ArrayList<>()));
    }

    /**
     * [특정 질문과 답변 조회 메서드]
     * 특정 질문 id로 질문 및 답변 조회
     */
    @Transactional
    public FindQuestionResponse findQuestionById(
            final int pageNo,
            final String criterion,
            final long questionId
    ) {
        Question question = questionRepository.findRegisteredQuestion(questionId)
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(questionId));

        question.updateViewCount(question.getViewCount() + 1);
        return FindQuestionResponse.of(question, answerService.findAllAnswersByQuestionId(pageNo, criterion, questionId));
    }

    /**
     * [관리자 전용: 전체 질문 조회 메서드]
     */
    @Transactional(readOnly = true)
    public List<FindAllQuestionsResponse> findAllQuestions(
            final int pageNo,
            final String criterion
    ) {
        Page<Question> questions = questionRepository.findAllQuestions(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion))
                .orElseThrow(QuestionException.QuestionNotFoundException::new);

        return questions.getContent().stream()
                .map(FindAllQuestionsResponse::from)
                .toList();
    }

    /**
     * [관리자 전용: 특정 상태 질문 조회 메서드]
     * 특정 상태(등록/삭제)의 회사를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public List<FindAllQuestionsResponse> findQuestionsByStatus(
            final int pageNo,
            final String criterion,
            final QuestionStatus questionStatus
    ) {
        Page<Question> questions = questionRepository.findAllByQuestionStatus(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion), questionStatus)
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(questionStatus));

        return questions.getContent().stream()
                .map(FindAllQuestionsResponse::from)
                .toList();
    }

    /**
     * [질문 등록 메서드]
     */
    @Transactional
    public void saveQuestion(
            final SaveQuestionRequest request,
            final String memberId
    ) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(request.companyId()));

        currencyService.writeQuestion(memberId, request.reward());
        questionRepository.save(new Question(request, memberId));
        company.updateQuestionCount(company.getQuestionCount() + 1);
    }

    /**
     * [질문 수정 메서드]
     */
    @Transactional
    public void updateQuestion(final UpdateQuestionRequest request) {
        Question question = questionRepository.findById(request.questionId())
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(request.questionId()));

        question.updateQuestion(new Question(request));
    }

    /**
     * [관리자 전용: 질문 삭제 메서드]
     */
    @Transactional
    public void deleteQuestion(final long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(questionId));

        question.updateQuestionStatus(QuestionStatus.DELETION);
    }
}
