package com.coverflow.feedback.infrastructure;

import com.coverflow.feedback.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("""
            SELECT f
            FROM Feedback f
            """)
    Optional<Page<Feedback>> findAllFeedbacks(final Pageable name);
}
