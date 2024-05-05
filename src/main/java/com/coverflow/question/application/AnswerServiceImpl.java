package com.coverflow.question.application;

import com.coverflow.notification.application.NotificationService;
import com.coverflow.notification.domain.Notification;
import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.Question;
import com.coverflow.question.dto.AnswerDTO;
import com.coverflow.question.dto.AnswerListDTO;
import com.coverflow.question.dto.MyAnswerDTO;
import com.coverflow.question.dto.request.FindAnswerAdminRequest;
import com.coverflow.question.dto.request.SaveAnswerRequest;
import com.coverflow.question.dto.request.UpdateAnswerRequest;
import com.coverflow.question.dto.request.UpdateSelectionRequest;
import com.coverflow.question.dto.response.FindAnswerResponse;
import com.coverflow.question.dto.response.FindMyAnswersResponse;
import com.coverflow.question.exception.AnswerException;
import com.coverflow.question.infrastructure.AnswerRepository;
import com.coverflow.question.infrastructure.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.global.constant.Constant.NORMAL_PAGE_SIZE;
import static com.coverflow.global.util.PageUtil.generatePageDesc;
import static com.coverflow.question.exception.AnswerException.AnswerNotFoundException;
import static com.coverflow.question.exception.QuestionException.QuestionNotFoundException;

@RequiredArgsConstructor
@Service
public class AnswerServiceImpl implements AnswerService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional(readOnly = true)
    public AnswerListDTO findByQuestionId(
            final int pageNo,
            final String criterion,
            final long questionId
    ) {
        Optional<Page<Answer>> answerList = answerRepository.findByQuestionIdAndAnswerStatus(generatePageDesc(pageNo, NORMAL_PAGE_SIZE, criterion), questionId);

        return answerList
                .map(answerPage ->
                        new AnswerListDTO(answerPage.getTotalPages(), answerPage.getContent().stream().map(AnswerDTO::from).toList())
                )
                .orElseGet(() -> new AnswerListDTO(0, new ArrayList<>()));
    }

    @Override
    @Transactional(readOnly = true)
    public FindMyAnswersResponse findByMemberId(
            final int pageNo,
            final String criterion,
            final UUID memberId
    ) {
        Page<Answer> answers = answerRepository.findRegisteredAnswers(generatePageDesc(pageNo, NORMAL_PAGE_SIZE, criterion), memberId)
                .orElseThrow(() -> new AnswerNotFoundException(memberId));

        return FindMyAnswersResponse.of(
                answers.getTotalPages(),
                answers.getTotalElements(),
                answers.getContent().stream().map(MyAnswerDTO::from).toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public FindAnswerResponse find(
            final int pageNo,
            final String criterion,
            final FindAnswerAdminRequest request
    ) {
        Page<Answer> answers = answerRepository.findWithFilters(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion), request)
                .orElseThrow(AnswerNotFoundException::new);

        return FindAnswerResponse.of(
                answers.getTotalPages(),
                answers.getTotalElements(),
                answers.getContent()
                        .stream()
                        .map(AnswerDTO::from)
                        .toList()
        );
    }

    @Override
    @Transactional
    public void save(
            final SaveAnswerRequest request,
            final String memberId
    ) {
        Question question = questionRepository.findById(request.questionId())
                .orElseThrow(() -> new QuestionNotFoundException(request.questionId()));

        if (String.valueOf(question.getMember().getId()).equals(memberId)) {
            throw new AnswerException.QuestionAuthorException(memberId);
        }

        question.updateAnswerCount(question.getAnswerCount() + 1);
        answerRepository.save(new Answer(request, memberId));
        notificationService.send(new Notification(question));
    }

    @Override
    @Transactional
    public void choose(
            final long answerId,
            final UpdateSelectionRequest request,
            final String memberId
    ) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException(answerId));

        if (!String.valueOf(answer.getQuestion().getMember().getId()).equals(memberId)) {
            throw new AnswerException.SelectionException();
        }

        answer.updateSelection(request.selection());
        answer.getMember().updateFishShapedBun(answer.getMember().getFishShapedBun() + answer.getQuestion().getReward());
        notificationService.send(new Notification(answer));
    }

    @Override
    @Transactional
    public void update(
            final long answerId,
            final UpdateAnswerRequest request
    ) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException(answerId));

        answer.updateAnswer(request);
    }

    @Override
    @Transactional
    public void delete(final long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException(answerId));

        answerRepository.delete(answer);
    }
}
