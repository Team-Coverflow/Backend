package com.coverflow.question.application;

import com.coverflow.member.domain.Member;
import com.coverflow.member.exception.MemberException;
import com.coverflow.member.infrastructure.MemberRepository;
import com.coverflow.notification.application.NotificationService;
import com.coverflow.notification.domain.Notification;
import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.AnswerStatus;
import com.coverflow.question.domain.Question;
import com.coverflow.question.dto.AnswerDTO;
import com.coverflow.question.dto.AnswerListDTO;
import com.coverflow.question.dto.AnswersDTO;
import com.coverflow.question.dto.MyAnswerDTO;
import com.coverflow.question.dto.request.SaveAnswerRequest;
import com.coverflow.question.dto.request.UpdateAnswerRequest;
import com.coverflow.question.dto.request.UpdateSelectionRequest;
import com.coverflow.question.dto.response.FindAnswerResponse;
import com.coverflow.question.dto.response.FindMyAnswersResponse;
import com.coverflow.question.exception.AnswerException;
import com.coverflow.question.exception.QuestionException;
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

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final NotificationService notificationService;

    /**
     * [특정 질문에 대한 답변 조회 메서드]
     */
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

    /**
     * [내 답변 목록 조회 메서드]
     */
    public FindMyAnswersResponse findByMemberId(
            final int pageNo,
            final String criterion,
            final UUID memberId
    ) {
        Page<Answer> answers = answerRepository.findRegisteredAnswers(generatePageDesc(pageNo, NORMAL_PAGE_SIZE, criterion), memberId)
                .orElseThrow(() -> new AnswerException.AnswerNotFoundException(memberId));

        return FindMyAnswersResponse.of(
                answers.getTotalPages(),
                answers.getContent().stream().map(MyAnswerDTO::from).toList()
        );
    }

    /**
     * [관리자 전용: 전체 답변 조회 메서드]
     */
    @Transactional(readOnly = true)
    public FindAnswerResponse find(
            final int pageNo,
            final String criterion
    ) {
        Page<Answer> answers = answerRepository.find(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion))
                .orElseThrow(AnswerException.AnswerNotFoundException::new);

        return FindAnswerResponse.of(
                answers.getTotalPages(),
                answers.getContent().stream()
                        .map(AnswersDTO::from)
                        .toList()
        );
    }

    /**
     * [관리자 전용: 특정 상태 답변 조회 메서드]
     * 특정 상태(등록/삭제)의 회사를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public FindAnswerResponse findByStatus(
            final int pageNo,
            final String criterion,
            final AnswerStatus answerStatus
    ) {
        Page<Answer> answers = answerRepository.findByAnswerStatus(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion), answerStatus)
                .orElseThrow(() -> new AnswerException.AnswerNotFoundException(answerStatus));

        return FindAnswerResponse.of(
                answers.getTotalPages(),
                answers.getContent().stream()
                        .map(AnswersDTO::from)
                        .toList()
        );
    }

    /**
     * [답변 등록 메서드]
     */
    @Transactional
    public void save(
            final SaveAnswerRequest request,
            final String memberId
    ) {
        Question question = questionRepository.findById(request.questionId())
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(request.questionId()));

        answerRepository.save(new Answer(request, memberId));
        notificationService.sendNotification(new Notification(question));
        question.updateAnswerCount(question.getAnswerCount() + 1);
    }

    /**
     * [답변 채택 메서드]
     */
    @Transactional
    public void choose(final UpdateSelectionRequest request) {
        Answer answer = answerRepository.findById(request.answerId())
                .orElseThrow(() -> new AnswerException.AnswerNotFoundException(request.answerId()));
        Member member = memberRepository.findById(answer.getMember().getId())
                .orElseThrow(() -> new MemberException.MemberNotFoundException(answer.getMember().getId()));

        answer.updateSelection(request.selection());
        member.updateFishShapedBun(member.getFishShapedBun() + answer.getQuestion().getReward());
        notificationService.sendNotification(new Notification(answer, member));
    }

    /**
     * [관리자 전용: 답변 수정 메서드]
     */
    @Transactional
    public void update(final UpdateAnswerRequest request) {
        Answer answer = answerRepository.findById(request.answerId())
                .orElseThrow(() -> new AnswerException.AnswerNotFoundException(request.answerId()));

        answer.updateAnswer(new Answer(request.content()));
    }

    /**
     * [관리자 전용: 답변 삭제 메서드]
     */
    @Transactional
    public void delete(final long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException.AnswerNotFoundException(answerId));

        answer.updateAnswerStatus(AnswerStatus.DELETION);
    }
}
