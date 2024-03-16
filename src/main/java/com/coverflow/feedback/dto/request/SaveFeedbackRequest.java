package com.coverflow.feedback.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SaveFeedbackRequest(
        @NotBlank
        String content
) {
}
