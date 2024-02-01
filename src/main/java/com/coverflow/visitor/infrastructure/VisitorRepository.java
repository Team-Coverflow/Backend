package com.coverflow.visitor.infrastructure;

import com.coverflow.visitor.domain.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VisitorRepository extends JpaRepository<Visitor, Integer> {
    Optional<Visitor> findByToday(final LocalDateTime today);
}
