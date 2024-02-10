package com.coverflow.question.infrastructure;

import com.coverflow.question.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<List<Question>> findAllQuestionsByCompanyIdAndStatus(
            final Long id,
            final String status
    );

    @Query("SELECT q " +
            "FROM Question q " +
            "ORDER BY q.createdAt ASC")
    Optional<List<Question>> findAllQuestions();

    @Query("SELECT q " +
            "FROM Question q " +
            "JOIN FETCH q.answers a " +
            "WHERE q.id = :questionId " +
            "AND a.status != '삭제' " +
            "ORDER BY a.createdAt ASC")
    Optional<Question> findByIdWithAnswers(@Param("questionId") Long questionId);
}
