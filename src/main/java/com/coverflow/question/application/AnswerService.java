package com.coverflow.question.application;

import com.coverflow.member.domain.Member;
import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.Question;
import com.coverflow.question.dto.request.SaveAnswerRequest;
import com.coverflow.question.dto.request.UpdateAnswerRequest;
import com.coverflow.question.dto.response.FindAnswerResponse;
import com.coverflow.question.exception.AnswerException;
import com.coverflow.question.infrastructure.AnswerRepository;
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

    private final AnswerRepository answerRepository;

    /**
     * [특정 질문에 대한 전체 답변 조회 메서드]
     */
    public List<FindAnswerResponse> findAnswer(final Long id) {
        final List<Answer> answers = answerRepository.findAllAnswersByQuestionIdAndStatus(id, "등록")
                .orElseThrow(() -> new AnswerException.AnswerNotFoundException(id));
        final List<FindAnswerResponse> findAnswers = new ArrayList<>();

        for (int i = 0; i < answers.size(); i++) {
            findAnswers.add(i, FindAnswerResponse.from(answers.get(i)));
        }
        return findAnswers;
    }

    /**
     * [관리자 전용: 특정 답변 조회 메서드]
     */
    public FindAnswerResponse findById(final Long id) {
        final Answer answer = answerRepository.findByIdAndStatus(id, "등록")
                .orElseThrow(() -> new AnswerException.AnswerNotFoundException(id));
        return FindAnswerResponse.from(answer);
    }

    /**
     * [답변 등록 메서드]
     */
    public void saveAnswer(
            final SaveAnswerRequest request,
            final String memberId
    ) {
        final Answer answer = Answer.builder()
                .content(request.content())
                .status("등록")
                .question(Question.builder()
                        .id(request.questionId())
                        .build())
                .member(Member.builder()
                        .id(UUID.fromString(memberId))
                        .build())
                .build();

        answerRepository.save(answer);
    }

    /**
     * [관리자 전용: 답변 수정 메서드]
     */
    @Transactional
    public void updateAnswer(final UpdateAnswerRequest request) {
        final Answer answer = answerRepository.findById(request.id())
                .orElseThrow(() -> new AnswerException.AnswerNotFoundException(request.id()));

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
