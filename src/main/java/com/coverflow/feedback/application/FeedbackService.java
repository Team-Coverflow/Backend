package com.coverflow.feedback.application;

import com.coverflow.feedback.dto.request.SaveFeedbackRequest;
import com.coverflow.feedback.dto.response.FeedbackResponse;

public interface FeedbackService {

    /**
     * [관리자: 전체 피드백 조회 메서드]
     */
    FeedbackResponse find(final int pageNo, final String criterion);

    /**
     * [피드백 등록 메서드]
     */
    void save(final SaveFeedbackRequest request);

    /**
     * [관리자: 특정 피드백 삭제 메서드]
     */
    void delete(final long feedbackId);
}
