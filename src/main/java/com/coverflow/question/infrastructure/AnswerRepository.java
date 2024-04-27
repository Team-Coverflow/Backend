package com.coverflow.question.infrastructure;

import com.coverflow.question.domain.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerCustomRepository {

    Optional<Answer> findByIdAndAnswerStatus(
            final long id,
            final boolean answerStatus
    );

    @Query("""
            SELECT a
            FROM Answer a
            WHERE a.question.id = :questionId
            AND a.answerStatus = true
            """)
    Optional<Page<Answer>> findByQuestionIdAndAnswerStatus(
            final Pageable pageable,
            @Param("questionId") final long questionId
    );

    @Query("""
            SELECT a
            FROM Answer a
            WHERE a.member.id = :memberId
            AND a.answerStatus = true
            ORDER BY a.createdAt DESC
            """)
    Optional<Page<Answer>> findRegisteredAnswers(
            final Pageable pageable,
            @Param("memberId") final UUID memberId
    );

    void deleteByMemberId(UUID id);
}
