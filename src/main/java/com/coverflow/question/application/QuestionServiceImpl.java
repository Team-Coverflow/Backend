package com.coverflow.question.application;

import com.coverflow.company.domain.Company;
import com.coverflow.company.infrastructure.CompanyRepository;
import com.coverflow.member.application.MemberServiceImpl;
import com.coverflow.question.domain.Question;
import com.coverflow.question.dto.AnswerListDTO;
import com.coverflow.question.dto.CompanyAndQuestionDTO;
import com.coverflow.question.dto.MyQuestionDTO;
import com.coverflow.question.dto.QuestionDTO;
import com.coverflow.question.dto.request.FindQuestionAdminRequest;
import com.coverflow.question.dto.request.SaveQuestionRequest;
import com.coverflow.question.dto.request.UpdateQuestionRequest;
import com.coverflow.question.dto.response.FindAllQuestionsResponse;
import com.coverflow.question.dto.response.FindMyQuestionsResponse;
import com.coverflow.question.dto.response.FindQuestionResponse;
import com.coverflow.question.infrastructure.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.coverflow.company.exception.CompanyException.CompanyNotFoundException;
import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.global.constant.Constant.NORMAL_PAGE_SIZE;
import static com.coverflow.global.util.PageUtil.generatePageDesc;
import static com.coverflow.question.exception.QuestionException.AlreadySelectedQuestionException;
import static com.coverflow.question.exception.QuestionException.QuestionNotFoundException;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

    private final MemberServiceImpl memberService;
    private final AnswerServiceImpl answerService;
    private final CompanyRepository companyRepository;
    private final QuestionRepository questionRepository;

    @Override
    @Transactional(readOnly = true)
    public CompanyAndQuestionDTO findByCompanyId(
            final int pageNo,
            final String criterion,
            final long companyId,
            final String questionTag
    ) {
        Optional<Page<Question>> questionList = questionRepository.findRegisteredQuestionsById(generatePageDesc(pageNo, NORMAL_PAGE_SIZE, criterion), companyId, questionTag);

        return questionList
                .map(questionPage ->
                        new CompanyAndQuestionDTO(
                                questionPage.getTotalPages(),
                                questionPage.getTotalElements(),
                                questionPage.getContent().stream().map(QuestionDTO::from).toList()
                        )
                )
                .orElseGet(() -> new CompanyAndQuestionDTO(0, 0, new ArrayList<>()));
    }

    @Override
    @Transactional(readOnly = true)
    public FindMyQuestionsResponse findByMemberId(
            final int pageNo,
            final String criterion,
            final UUID memberId
    ) {
        Page<Question> questionList = questionRepository.findRegisteredQuestionsByMemberId(generatePageDesc(pageNo, NORMAL_PAGE_SIZE, criterion), memberId)
                .orElseThrow(() -> new QuestionNotFoundException(memberId));

        return FindMyQuestionsResponse.of(
                questionList.getTotalPages(),
                questionList.getTotalElements(),
                questionList.getContent().stream().map(MyQuestionDTO::from).toList()
        );
    }

    @Override
    @Transactional
    public FindQuestionResponse findById(
            final int pageNo,
            final String criterion,
            final long questionId
    ) {
        Question question = questionRepository.findRegisteredQuestion(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));

        question.updateViewCount(question.getViewCount() + 1);

        AnswerListDTO answerList = answerService.findByQuestionId(pageNo, criterion, questionId);

        return FindQuestionResponse.of(question, answerList.getTotalPages(), answerList.getAnswers());
    }

    @Override
    @Transactional(readOnly = true)
    public FindAllQuestionsResponse find(
            final int pageNo,
            final String criterion,
            final FindQuestionAdminRequest request
    ) {
        Page<Question> questions = questionRepository.findWithFilters(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion), request)
                .orElseThrow(() -> new QuestionNotFoundException(request));

        return FindAllQuestionsResponse.of(
                questions.getTotalPages(),
                questions.getTotalElements(),
                questions.getContent().stream()
                        .map(QuestionDTO::from)
                        .toList()
        );
    }

    @Override
    @Transactional
    public void save(
            final SaveQuestionRequest request,
            final String memberId
    ) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new CompanyNotFoundException(request.companyId()));

        memberService.writeQuestion(memberId, request.reward());
        questionRepository.save(new Question(request, memberId));
    }

    @Override
    @Transactional
    public void update(
            final long questionId,
            final UpdateQuestionRequest request
    ) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));

        if (question.isSelectionStatus()) {
            throw new AlreadySelectedQuestionException(questionId);
        }

        question.updateQuestion(request);
    }

    @Override
    @Transactional
    public void delete(final long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));

        questionRepository.delete(question);
    }
}
