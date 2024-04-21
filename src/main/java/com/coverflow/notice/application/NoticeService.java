package com.coverflow.notice.application;

import com.coverflow.notice.domain.Notice;
import com.coverflow.notice.dto.request.SaveNoticeRequest;
import com.coverflow.notice.dto.request.UpdateNoticeRequest;
import com.coverflow.notice.dto.response.FindNoticeResponse;
import com.coverflow.notice.infrastructure.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    public FindNoticeResponse find(
            final int pageNo,
            final String criterion
    ) {
        Page<Notice> notices = noticeRepository.findById()
    }

    public void save(
            final SaveNoticeRequest request,
            final String username
    ) {
    }

    public void update(
            final long noticeId,
            final UpdateNoticeRequest request
    ) {
    }

    public void delete(final long noticeId) {
    }
}
