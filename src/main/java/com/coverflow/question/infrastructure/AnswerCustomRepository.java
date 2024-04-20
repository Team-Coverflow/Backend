package com.coverflow.question.infrastructure;

import com.coverflow.question.domain.Answer;
import com.coverflow.question.dto.request.FindAnswerAdminRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AnswerCustomRepository {

    Optional<Page<Answer>> findWithFilters(final Pageable pageable, final FindAnswerAdminRequest request);
}
