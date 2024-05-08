package com.coverflow.question.infrastructure;

import com.coverflow.question.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionCustomRepository {

    @Query("""
            SELECT q
            FROM Question q
            LEFT JOIN FETCH q.answers
            WHERE q.member.id = :memberId
            AND q.questionStatus = true
            ORDER BY q.createdAt DESC
            """)
    Optional<Page<Question>> findRegisteredQuestionsByMemberId(
            final Pageable pageable,
            @Param("memberId") final UUID memberId
    );

    @Query("""
            SELECT DISTINCT q
            FROM Question q
            LEFT JOIN FETCH q.answers
            WHERE q.id = :questionId
            AND q.questionStatus = true
            """)
    Optional<Question> findRegisteredQuestion(@Param("questionId") final long questionId);

    void deleteByMemberId(UUID id);
}
