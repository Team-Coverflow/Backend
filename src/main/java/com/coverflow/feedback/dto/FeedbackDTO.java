package com.coverflow.feedback.dto;

import com.coverflow.feedback.domain.Feedback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {
    private long id;
    private String content;

    public static FeedbackDTO from(final Feedback feedback) {
        return new FeedbackDTO(
                feedback.getId(),
                feedback.getContent()
        );
    }
}
