package com.coverflow.board.application;

import com.coverflow.board.domain.Question;
import com.coverflow.board.dto.response.QuestionResponse;
import com.coverflow.board.exception.QuestionException;
import com.coverflow.board.infrastructure.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    /**
     * [특정 질문 글 조회 메서드]
     * 질문 글의 id로 조회
     */
    public QuestionResponse findQuestionById(final Long id) {
        final Question question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(id));
        return QuestionResponse.of(question);
    }

    /**
     * [특정 회사의 전체 질문 글 조회 메서드]
     * 회사 id로 조회
     */
    public List<QuestionResponse> findAllQuestionsByCompanyId(final Long id) {
        final List<Question> questions = questionRepository.findAllQuestionsByCompanyIdAndStatus(id, "등록")
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(id));
        final List<QuestionResponse> findQuestions = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            findQuestions.add(i, QuestionResponse.of(questions.get(i)));
        }
        return findQuestions;
    }

    /**
     * [관리자 전용: 전체 질문 글 조회 메서드]
     * 회사 id로 조회
     */
    public List<QuestionResponse> findAllQuestions() {
        final List<Question> questions = questionRepository.findAllQuestions()
                .orElseThrow(QuestionException.QuestionNotFoundException::new);
        final List<QuestionResponse> findQuestions = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            findQuestions.add(i, QuestionResponse.of(questions.get(i)));
        }
        return findQuestions;
    }
}
