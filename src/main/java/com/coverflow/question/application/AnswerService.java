package com.coverflow.question.application;

import com.coverflow.member.domain.Member;
import com.coverflow.member.exception.MemberException;
import com.coverflow.member.infrastructure.MemberRepository;
import com.coverflow.notification.application.NotificationService;
import com.coverflow.notification.domain.Notification;
import com.coverflow.notification.domain.NotificationType;
import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.Question;
import com.coverflow.question.dto.AnswerDTO;
import com.coverflow.question.dto.request.SaveAnswerRequest;
import com.coverflow.question.dto.request.UpdateAnswerRequest;
import com.coverflow.question.dto.request.UpdateSelectionRequest;
import com.coverflow.question.dto.response.FindAnswerResponse;
import com.coverflow.question.exception.AnswerException;
import com.coverflow.question.exception.QuestionException;
import com.coverflow.question.infrastructure.AnswerRepository;
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

@Transactional(readOnly = true)
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
    public List<AnswerDTO> findAllAnswersByQuestionId(
            final Long questionId,
            final int pageNo,
            final String criterion
    ) {
        final List<AnswerDTO> answers = new ArrayList<>();
        final Pageable pageable = PageRequest.of(pageNo, 5, Sort.by(criterion).descending());
        final Optional<Page<Answer>> optionalAnswers = answerRepository.findAllAnswersByQuestionIdAndStatus(questionId, pageable);

        if (optionalAnswers.isPresent()) {
            Page<Answer> answerList = optionalAnswers.get();
            for (int i = 0; i < answerList.getContent().size(); i++) {
                answers.add(i, new AnswerDTO(
                        answerList.getContent().get(i).getId(),
                        answerList.getContent().get(i).getMember().getNickname(),
                        answerList.getContent().get(i).getMember().getTag(),
                        answerList.getContent().get(i).getContent(),
                        answerList.getContent().get(i).getCreatedAt()));
            }
        }
        return answers;
    }

    /**
     * [관리자 전용: 전체 답변 조회 메서드]
     */
    public List<FindAnswerResponse> findAllAnswers(
            final int pageNo,
            final String criterion
    ) {
        final Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(criterion).descending());
        final Page<Answer> answers = answerRepository.findAllAnswers(pageable)
                .orElseThrow(AnswerException.AnswerNotFoundException::new);
        final List<FindAnswerResponse> findAnswers = new ArrayList<>();

        for (int i = 0; i < answers.getContent().size(); i++) {
            findAnswers.add(i, FindAnswerResponse.from(answers.getContent().get(i)));
        }
        return findAnswers;
    }

    /**
     * [관리자 전용: 특정 답변 조회 메서드]
     */
    public FindAnswerResponse findById(final Long answerId) {
        final Answer answer = answerRepository.findByIdAndStatus(answerId, "등록")
                .orElseThrow(() -> new AnswerException.AnswerNotFoundException(answerId));
        return FindAnswerResponse.from(answer);
    }

    /**
     * [답변 등록 메서드]
     */
    @Transactional
    public void saveAnswer(
            final SaveAnswerRequest request,
            final String memberId
    ) {
        final Question question = questionRepository.findById(request.questionId())
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(request.questionId()));
        final Answer answer = Answer.builder()
                .content(request.content())
                .selection(false)
                .status("등록")
                .question(Question.builder()
                        .id(request.questionId())
                        .build())
                .member(Member.builder()
                        .id(UUID.fromString(memberId))
                        .build())
                .build();
        final Notification notification = Notification.builder()
                .content(question.getCompany().getName())
                .url("/company-info/" +
                        question.getCompany().getId().toString() +
                        "/" +
                        question.getId().toString())
                .type(NotificationType.ANSWER)
                .status("안읽음")
                .member(question.getMember())
                .build();

        answerRepository.save(answer);
        notificationService.sendNotification(notification);
        question.updateAnswerCount(question.getAnswerCount() + 1);
    }

    /**
     * [답변 채택 메서드]
     */
    @Transactional
    public void chooseAnswer(final UpdateSelectionRequest request) {
        final Answer answer = answerRepository.findById(request.answerId())
                .orElseThrow(() -> new AnswerException.AnswerNotFoundException(request.answerId()));
        final Member member = memberRepository.findById(answer.getMember().getId())
                .orElseThrow(() -> new MemberException.MemberNotFoundException(answer.getMember().getId()));
        final Notification notification = Notification.builder()
                .content(answer.getQuestion().getCompany().getName())
                .url("/company-info/" +
                        answer.getQuestion().getCompany().getId().toString() +
                        "/" +
                        answer.getQuestion().getId().toString())
                .type(NotificationType.SELECTION)
                .status("안읽음")
                .member(member)
                .build();

        answer.updateSelection(request.selection());
        member.updateFishShapedBun(member.getFishShapedBun() + answer.getQuestion().getReward());
        notificationService.sendNotification(notification);
    }

    /**
     * [관리자 전용: 답변 수정 메서드]
     */
    @Transactional
    public void updateAnswer(final UpdateAnswerRequest request) {
        final Answer answer = answerRepository.findById(request.answerId())
                .orElseThrow(() -> new AnswerException.AnswerNotFoundException(request.answerId()));

        answer.updateAnswer(Answer.builder()
                .content(request.content())
                .build());
    }

    /**
     * [관리자 전용: 답변 삭제 메서드]
     */
    @Transactional
    public void deleteAnswer(final Long answerId) {
        final Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException.AnswerNotFoundException(answerId));

        answer.updateStatus("삭제");
    }
}
