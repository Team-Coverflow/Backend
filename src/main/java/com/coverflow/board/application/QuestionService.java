package com.coverflow.board.application;

import com.coverflow.board.domain.Question;
import com.coverflow.board.dto.response.QuestionResponse;
import com.coverflow.board.exception.QuestionException;
import com.coverflow.board.infrastructure.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    /**
     * [특정 질문 글 조회 메서드]
     */
    public QuestionResponse findQuestionById(final long id) {
        final Question question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionException.QuestionNotFoundException(id));
        return QuestionResponse.of(question);
    }
}
