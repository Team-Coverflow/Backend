package com.coverflow.board.infrastructure;

import com.coverflow.board.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
