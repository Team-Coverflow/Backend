package com.coverflow.notice.dto.response;

import java.time.LocalDate;

public record FindNoticeResponse(
        long id,
        String title,
        String content,
        long views,
        LocalDate createdAt
) {
}
