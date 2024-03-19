package com.coverflow.feedback.dto;

import com.coverflow.feedback.domain.Feedback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {
    private long id;
    private String content;
    private LocalDate createdAt;

    public static FeedbackDTO from(final Feedback feedback) {
        return new FeedbackDTO(
                feedback.getId(),
                feedback.getContent(),
                feedback.getCreatedAt().toLocalDate()
        );
    }
}
