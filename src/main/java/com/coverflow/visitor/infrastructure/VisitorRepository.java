package com.coverflow.visitor.infrastructure;

import com.coverflow.visitor.domain.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Integer> {
    Optional<Visitor> findByToday(final String today);
}
