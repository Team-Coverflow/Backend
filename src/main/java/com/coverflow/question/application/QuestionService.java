package com.coverflow.question.application;

import com.coverflow.company.domain.Company;
import com.coverflow.company.exception.CompanyException;
import com.coverflow.company.infrastructure.CompanyRepository;
import com.coverflow.member.application.CurrencyService;
import com.coverflow.member.domain.Member;
import com.coverflow.question.domain.Question;
import com.coverflow.question.domain.QuestionStatus;
import com.coverflow.question.dto.QuestionDTO;
import com.coverflow.question.dto.request.SaveQuestionRequest;
import com.coverflow.question.dto.request.UpdateQuestionRequest;
import com.coverflow.question.dto.response.FindAllQuestionsResponse;
import com.coverflow.question.dto.response.FindQuestionResponse;
import com.coverflow.question.exception.QuestionException;
import com.coverflow.question.infrastructure.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.global.constant.Constant.SMALL_PAGE_SIZE;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final CurrencyService currencyService;
    private final AnswerService answerService;
    private final CompanyRepository companyRepository;
    private final QuestionRepository questionRepository;

    /**
     * [특정 회사의 질문 조회 메서드]
     * 회사 id로 조회
     */
    @Transactional(readOnly = true)
    public List<QuestionDTO> findAllQuestionsByCompanyId(
            final int pageNo,
            final String criterion,
            final long companyId
    ) {
        Pageable pageable = PageRequest.of(pageNo, SMALL_PAGE_SIZE, Sort.by(criterion).descending());
        Optional<Page<Question>> optionalQuestions = questionRepository.findRegisteredQuestions(pageable, companyId);
        List<QuestionDTO> questions = new ArrayList<>();

        if (optionalQuestions.isPresent()) {
            Page<Question> questionList = optionalQuestions.get();
            for (int i = 0; i < questionList.getContent().size(); i++) {
                questions.add(i, new QuestionDTO(
                        questionList.getContent().get(i).getId(),
                        questionList.getContent().get(i).getMember().getNickname(),
                        questionList.getContent().get(i).getMember().getTag(),
                        questionList.getContent().get(i).getTitle(),
                        questionList.getContent().get(i).getContent(),
                        questionList.getContent().get(i).getViewCount(),
                        questionList.getContent().get(i).getAnswerCount(),
                        questionList.getContent().get(i).getReward(),
                        questionList.getContent().get(i).getCreatedAt()));
            }
        }
        return questions;
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
        Pageable pageable = PageRequest.of(pageNo, LARGE_PAGE_SIZE, Sort.by(criterion).descending());
        Page<Question> questions = questionRepository.findAllQuestions(pageable)
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
        Pageable pageable = PageRequest.of(pageNo, LARGE_PAGE_SIZE, Sort.by(criterion).descending());
        Page<Question> questions = questionRepository.findAllByQuestionStatus(pageable, questionStatus)
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
        Question question = Question.builder()
                .title(request.title())
                .content(request.content())
                .viewCount(1)
                .answerCount(0)
                .reward(request.reward())
                .questionStatus(QuestionStatus.REGISTRATION)
                .company(Company.builder()
                        .id(request.companyId())
                        .build())
                .member(Member.builder()
                        .id(UUID.fromString(memberId))
                        .build())
                .build();

        currencyService.writeQuestion(memberId, request.reward());
        questionRepository.save(question);
        company.updateQuestionCount(company.getQuestionCount() + 1);
    }

    /**
     * [관리자 전용: 질문 수정 메서드]
     */
    @Transactional
    public void updateQuestion(final UpdateQuestionRequest request) {
        Question question = questionRepository.findById(request.questionId())
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
    public void deleteQuestion(final long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(questionId));

        question.updateStatus(QuestionStatus.DELETION);
    }
}
