package com.coverflow.question.infrastructure;

import com.coverflow.question.domain.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByIdAndStatus(
            final Long id,
            final String status
    );

    @Query("SELECT a " +
            "FROM Answer a " +
            "WHERE a.question.id = :questionId " +
            "AND a.status = '등록'")
    Optional<Page<Answer>> findAllAnswersByQuestionIdAndStatus(
            final Pageable pageable,
            @Param("questionId") final Long questionId
    );

    @Query("SELECT a " +
            "FROM Answer a ")
    Optional<Page<Answer>> findAllAnswers(final Pageable pageable);

    @Query("SELECT a " +
            "FROM Answer a " +
            "WHERE a.status = :status")
    Optional<Page<Answer>> findAllByStatus(
            final Pageable pageable,
            @Param("status") final String status
    );
}
