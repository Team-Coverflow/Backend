package com.coverflow.feedback.application;

import com.coverflow.feedback.domain.Feedback;
import com.coverflow.feedback.dto.FeedbackDTO;
import com.coverflow.feedback.dto.request.SaveFeedbackRequest;
import com.coverflow.feedback.dto.response.FeedbackResponse;
import com.coverflow.feedback.exception.FeedbackException;
import com.coverflow.feedback.infrastructure.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.global.util.PageUtil.generatePageDesc;

@RequiredArgsConstructor
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    /**
     * [관리자: 전체 피드백 조회 메서드]
     */
    @Transactional(readOnly = true)
    public FeedbackResponse find(
            final int pageNo,
            final String criterion
    ) {
        Page<Feedback> feedbacks = feedbackRepository.findAllFeedbacks(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion))
                .orElseThrow(FeedbackException.FeedbackNotFoundException::new);

        return FeedbackResponse.of(
                feedbacks.getTotalPages(),
                feedbacks.getTotalElements(),
                feedbacks.getContent()
                        .stream()
                        .map(FeedbackDTO::from)
                        .toList()
        );
    }

    /**
     * [피드백 등록 메서드]
     */
    @Transactional
    public void save(final SaveFeedbackRequest request) {
        feedbackRepository.save(new Feedback(request));
    }

    /**
     * [관리자: 특정 피드백 삭제 메서드]
     */
    @Transactional
    public void delete(final long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new FeedbackException.FeedbackNotFoundException(feedbackId));

        feedbackRepository.delete(feedback);
    }
}
