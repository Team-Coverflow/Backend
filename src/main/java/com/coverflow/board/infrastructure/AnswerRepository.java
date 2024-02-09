package com.coverflow.board.infrastructure;

import com.coverflow.board.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    Optional<List<Answer>> findAllAnswersByQuestionIdAndStatus(
            final Long id,
            final String status
    );
}
