package com.coverflow.feedback.application;

import com.coverflow.feedback.domain.Feedback;
import com.coverflow.feedback.dto.FeedbackDTO;
import com.coverflow.feedback.dto.response.FeedbackResponse;
import com.coverflow.feedback.exception.FeedbackException;
import com.coverflow.feedback.infrastructure.FeedbackRepository;
import org.springframework.data.domain.Page;

import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.global.util.PageUtil.generatePageDesc;

public class FeedbackService {

    private FeedbackRepository feedbackRepository;

    /**
     * [전체 피드백 조회 메서드]
     */
    public FeedbackResponse findFeedback(
            final int pageNo,
            final String criterion
    ) {
        Page<Feedback> feedbacks = feedbackRepository.findAll(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion))
                .orElseThrow(FeedbackException.FeedbackNotFoundException::new);

        return FeedbackResponse.of(
                feedbacks.getTotalPages(),
                feedbacks.getContent()
                        .stream()
                        .map(FeedbackDTO::from)
                        .toList()
        );
    }
}
