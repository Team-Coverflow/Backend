package com.coverflow.notice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SaveNoticeRequest(
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}
