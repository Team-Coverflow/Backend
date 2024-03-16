package com.coverflow.feedback.infrastructure;

import com.coverflow.feedback.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FeedbackRepository {

    @Query("""
            SELECT *
            FROM Feedback f
            """)
    Optional<Page<Feedback>> findAll(final Pageable name);
}
