package com.coverflow.notice.application;

import com.coverflow.notice.dto.request.SaveNoticeRequest;
import com.coverflow.notice.dto.request.UpdateNoticeRequest;
import com.coverflow.notice.dto.response.FindNoticeResponse;

public interface NoticeService {

    /**
     * [공지 조회 메서드]
     */
    FindNoticeResponse find(final int pageNo, final String criterion);

    /**
     * [공지 등록 메서드]
     */
    void save(final SaveNoticeRequest request, final String memberId);

    /**
     * [공지 수정 메서드]
     */
    void update(final long noticeId, final UpdateNoticeRequest request);

    /**
     * [공지 삭제 메서드]
     */
    void delete(final long noticeId);
}
