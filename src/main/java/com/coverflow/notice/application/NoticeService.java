package com.coverflow.notice.application;

import com.coverflow.notice.domain.Notice;
import com.coverflow.notice.dto.NoticeDTO;
import com.coverflow.notice.dto.request.SaveNoticeRequest;
import com.coverflow.notice.dto.request.UpdateNoticeRequest;
import com.coverflow.notice.dto.response.FindNoticeResponse;
import com.coverflow.notice.infrastructure.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.global.util.PageUtil.generatePageDesc;
import static com.coverflow.notice.exception.NoticeException.NoticeNotFoundException;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    /**
     * [공지 조회 메서드]
     */
    @Transactional(readOnly = true)
    public FindNoticeResponse find(
            final int pageNo,
            final String criterion
    ) {
        Page<Notice> notices = noticeRepository.findAll(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion))
                .orElseThrow(NoticeNotFoundException::new);

        return FindNoticeResponse.of(
                notices.getTotalPages(),
                notices.getTotalElements(),
                notices.getContent()
                        .stream()
                        .map(NoticeDTO::from)
                        .toList()
        );
    }

    /**
     * [공지 등록 메서드]
     */
    @Transactional
    public void save(
            final SaveNoticeRequest request,
            final String memberId
    ) {
        noticeRepository.save(new Notice(request, memberId));
    }

    /**
     * [공지 수정 메서드]
     */
    @Transactional
    public void update(
            final long noticeId,
            final UpdateNoticeRequest request
    ) {
    }

    /**
     * [공지 삭제 메서드]
     */
    @Transactional
    public void delete(final long noticeId) {
    }
}
