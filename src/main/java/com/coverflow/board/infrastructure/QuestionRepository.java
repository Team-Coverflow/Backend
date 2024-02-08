package com.coverflow.board.infrastructure;

import com.coverflow.board.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<List<Question>> findAllQuestionsByCompanyIdAndStatus(
            final Long id,
            final String status
    );

    @Query(value = "SELECT q FROM Question q ORDER BY q.createdAt ASC")
    Optional<List<Question>> findAllQuestions();
}
