package com.coverflow.board.application;

import com.coverflow.board.domain.Answer;
import com.coverflow.board.dto.response.FindAnswerResponse;
import com.coverflow.board.exception.AnswerException;
import com.coverflow.board.infrastructure.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public List<FindAnswerResponse> findAnswer(long id) {
        final List<Answer> answers = answerRepository.findAllAnswersByQuestionIdAndStatus(id, "등록")
                .orElseThrow(() -> new AnswerException.AnswerNotFoundException(id));
        final List<FindAnswerResponse> findAnswers = new ArrayList<>();

        for (int i = 0; i < answers.size(); i++) {
            findAnswers.add(i, FindAnswerResponse.of(answers.get(i)));
        }
        return findAnswers;
    }
}
