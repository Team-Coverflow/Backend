package com.coverflow.notice.dto;

import com.coverflow.notice.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {
    private Long noticeId;
    private String noticeTitle;
    private String noticeContent;
    private long noticeViews;
    private boolean noticeStatus;
    private LocalDateTime createdAt;

    public static NoticeDTO from(final Notice notice) {
        return new NoticeDTO(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getViews(),
                notice.isNoticeStatus(),
                notice.getCreatedAt()
        );
    }
}
