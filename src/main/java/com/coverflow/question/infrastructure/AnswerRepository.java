package com.coverflow.question.infrastructure;

import com.coverflow.question.domain.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("SELECT a " +
            "FROM Answer a " +
            "WHERE a.question.id = :questionId " +
            "AND a.status = '등록'")
    Optional<Page<Answer>> findAllAnswersByQuestionIdAndStatus(
            @Param("companyId") final Long companyId,
            final Pageable pageable
    );

    Optional<Answer> findByIdAndStatus(Long id, String status);

    Optional<Page<Answer>> findAllAnswers(final Pageable pageable);
}
