package com.coverflow.notice.dto.response;

import com.coverflow.notice.dto.NoticeDTO;

import java.util.List;

public record FindNoticeResponse(
        int totalPages,
        long totalElements,
        List<NoticeDTO> notices
) {
    public static FindNoticeResponse of(
            final int totalPages,
            final long totalElements,
            final List<NoticeDTO> notices
    ) {
        return new FindNoticeResponse(totalPages, totalElements, notices);
    }
}
