package com.coverflow.question.application;

import com.coverflow.member.domain.Member;
import com.coverflow.member.exception.MemberException;
import com.coverflow.member.infrastructure.MemberRepository;
import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.Question;
import com.coverflow.question.dto.request.SaveAnswerRequest;
import com.coverflow.question.dto.request.UpdateAnswerRequest;
import com.coverflow.question.dto.request.UpdateSelectionRequest;
import com.coverflow.question.dto.response.FindAnswerResponse;
import com.coverflow.question.exception.AnswerException;
import com.coverflow.question.exception.QuestionException;
import com.coverflow.question.infrastructure.AnswerRepository;
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
public class AnswerService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    /**
     * [특정 질문에 대한 전체 답변 조회 메서드]
     */
    public List<FindAnswerResponse> findAnswer(final Long questionId) {
        final List<Answer> answers = answerRepository.findAllAnswersByQuestionIdAndStatus(questionId, "등록")
                .orElseThrow(() -> new AnswerException.AnswerNotFoundException(questionId));
        final List<FindAnswerResponse> findAnswers = new ArrayList<>();

        for (int i = 0; i < answers.size(); i++) {
            findAnswers.add(i, FindAnswerResponse.from(answers.get(i)));
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

        answerRepository.save(answer);
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

        answer.updateSelection(request.selection());
        member.updateFishShapedBun(member.getFishShapedBun() + answer.getQuestion().getReward());
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
