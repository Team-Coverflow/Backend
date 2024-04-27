package com.coverflow.question.infrastructure;

import com.coverflow.question.domain.Question;
import com.coverflow.question.dto.request.FindQuestionAdminRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QuestionCustomRepository {

    Optional<Page<Question>> findRegisteredQuestions(final Pageable pageable, final long companyId);

    Optional<Page<Question>> findWithFilters(final Pageable pageable, final FindQuestionAdminRequest request);
}
