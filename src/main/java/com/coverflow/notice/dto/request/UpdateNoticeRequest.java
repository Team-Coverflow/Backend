package com.coverflow.notice.dto.request;

public record UpdateNoticeRequest(
        String title,
        String content,
        boolean noticeStatus
) {
}
