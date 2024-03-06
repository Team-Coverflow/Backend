package com.coverflow.question.infrastructure;

import com.coverflow.question.domain.Question;
import com.coverflow.question.domain.QuestionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("""
            SELECT q
            FROM Question q
            WHERE q.company.id = :companyId
            AND q.questionStatus = 'REGISTRATION'
            ORDER BY q.createdAt DESC
            """)
    Optional<Page<Question>> findRegisteredQuestions(
            final Pageable pageable,
            @Param("companyId") final long companyId
    );

    @Query("""
            SELECT DISTINCT q
            FROM Question q
            WHERE q.id = :questionId
            AND q.questionStatus = 'REGISTRATION'
            """)
    Optional<Question> findRegisteredQuestion(@Param("questionId") final long questionId);

    @Query("""
            SELECT q
            FROM Question q
            """)
    Optional<Page<Question>> findAllQuestions(final Pageable pageable);

    @Query("""
            SELECT q
            FROM Question q
            WHERE q.questionStatus = :questionStatus
            """)
    Optional<Page<Question>> findAllByQuestionStatus(
            final Pageable pageable,
            @Param("questionStatus") final QuestionStatus questionStatus
    );
}
