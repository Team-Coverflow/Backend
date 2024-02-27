package com.coverflow.question.infrastructure;

import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Optional<Page<Question>> findAllQuestions(final Pageable pageable);

    @Query("SELECT q " +
            "FROM Question q " +
            "WHERE q.status = :status")
    Optional<Page<Question>> findAllByStatus(
            @Param("status") final String status,
            final Pageable pageable
    );

    @Query("SELECT q " +
            "FROM Question q " +
            "WHERE q.company.id = :companyId " +
            "AND q.status = '등록' " +
            "ORDER BY q.createdAt DESC")
    Optional<Page<Question>> findRegisteredQuestions(
            @Param("companyId") final Long companyId,
            final Pageable pageable
    );

    @Query("SELECT DISTINCT q " +
            "FROM Question q " +
            "WHERE q.id = :questionId " +
            "AND q.status = '등록'")
    Optional<Question> findRegisteredQuestion(@Param("questionId") final Long questionId);

    @Query("SELECT a " +
            "FROM Answer a " +
            "WHERE a.question.id = :questionId " +
            "AND a.status = '등록' " +
            "ORDER BY a.createdAt DESC")
    Optional<List<Answer>> findRegisteredAnswers(@Param("questionId") final Long questionId);
}
