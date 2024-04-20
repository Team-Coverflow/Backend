package com.coverflow.inquiry.application;

import com.coverflow.inquiry.domain.Inquiry;
import com.coverflow.inquiry.dto.InquiriesDTO;
import com.coverflow.inquiry.dto.InquiryCountDTO;
import com.coverflow.inquiry.dto.InquiryDTO;
import com.coverflow.inquiry.dto.request.FindInquiryAdminRequest;
import com.coverflow.inquiry.dto.request.SaveInquiryRequest;
import com.coverflow.inquiry.dto.request.UpdateInquiryRequest;
import com.coverflow.inquiry.dto.response.FindAllInquiriesResponse;
import com.coverflow.inquiry.dto.response.FindInquiryResponse;
import com.coverflow.inquiry.infrastructure.InquiryRepository;
import com.coverflow.notification.application.NotificationService;
import com.coverflow.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.global.constant.Constant.NORMAL_PAGE_SIZE;
import static com.coverflow.global.util.PageUtil.generatePageDesc;
import static com.coverflow.inquiry.domain.InquiryStatus.COMPLETE;
import static com.coverflow.inquiry.domain.InquiryStatus.WAIT;
import static com.coverflow.inquiry.exception.InquiryException.InquiryNotFoundException;

@RequiredArgsConstructor
@Service
public class InquiryService {

    private final NotificationService notificationService;
    private final InquiryRepository inquiryRepository;

    /**
     * [특정 회원의 문의 조회 메서드]
     */
    @Transactional(readOnly = true)
    public FindInquiryResponse findMyInquiries(
            final int pageNo,
            final String criterion,
            final String memberId
    ) {
        Page<Inquiry> inquiries = inquiryRepository.findAllByMemberIdAndStatus(generatePageDesc(pageNo, NORMAL_PAGE_SIZE, criterion), UUID.fromString(memberId))
                .orElseThrow(() -> new InquiryNotFoundException(memberId));
        int waitInquiryCount = inquiryRepository.findAllCountByMemberId(UUID.fromString(memberId), WAIT);
        int completeInquiryCount = inquiryRepository.findAllCountByMemberId(UUID.fromString(memberId), COMPLETE);
        int allInquiryCount = waitInquiryCount + completeInquiryCount;

        InquiryCountDTO inquiryCountDTO = new InquiryCountDTO(allInquiryCount, waitInquiryCount, completeInquiryCount);

        return FindInquiryResponse.of(
                inquiries.getTotalPages(),
                inquiries.getTotalElements(),
                inquiries.getContent()
                        .stream()
                        .map(inquiry -> InquiryDTO.of(inquiry, inquiryCountDTO))
                        .toList()
        );
    }

    /**
     * [관리자 - 문의 조회 메서드]
     */
    @Transactional(readOnly = true)
    public FindAllInquiriesResponse find(
            final int pageNo,
            final String criterion,
            final FindInquiryAdminRequest request
    ) {
        Page<Inquiry> inquiries = inquiryRepository.findWithFilters(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion), request)
                .orElseThrow(() -> new InquiryNotFoundException(request));

        return FindAllInquiriesResponse.of(
                inquiries.getTotalPages(),
                inquiries.getTotalElements(),
                inquiries.getContent()
                        .stream()
                        .map(InquiriesDTO::from)
                        .toList()
        );
    }

    /**
     * [문의 등록 메서드]
     */
    @Transactional
    public void save(
            final SaveInquiryRequest request,
            final String memberId
    ) {
        inquiryRepository.save(new Inquiry(request, memberId));
    }

    /**
     * [관리자 - 문의 수정 메서드]
     */
    @Transactional
    public void update(
            final long inquiryId,
            final UpdateInquiryRequest request
    ) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryNotFoundException(inquiryId));

        inquiry.updateInquiry(request);
        notificationService.send(new Notification(inquiry));
    }

    /**
     * [관리자 - 문의 삭제 메서드]
     */
    @Transactional
    public void delete(final long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryNotFoundException(inquiryId));

        inquiryRepository.delete(inquiry);
    }
}
