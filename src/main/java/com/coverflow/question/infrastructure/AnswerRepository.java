package com.coverflow.question.infrastructure;

import com.coverflow.question.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<List<Answer>> findAllAnswersByQuestionIdAndStatus(
            final Long id,
            final String status
    );

    @Query("SELECT a " +
            "FROM Answer a " +
            "WHERE a.question.id = :questionId " +
            "AND a.status != '삭제'")
    List<Answer> findByQuestionIdAndStatusIsNotDeleted(@Param("questionId") Long questionId);

    Optional<Answer> findByIdAndStatus(Long id, String status);

    @Query("SELECT a " +
            "FROM Answer a " +
            "WHERE a.status = '등록'")
    Optional<List<Answer>> findAnswers();
}
