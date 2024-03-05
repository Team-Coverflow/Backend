package com.coverflow.Inquiry.application;

import com.coverflow.Inquiry.domain.Inquiry;
import com.coverflow.Inquiry.dto.request.SaveInquiryRequest;
import com.coverflow.Inquiry.dto.request.UpdateInquiryRequest;
import com.coverflow.Inquiry.dto.response.FindAllInquiriesResponse;
import com.coverflow.Inquiry.dto.response.FindInquiryResponse;
import com.coverflow.Inquiry.exception.InquiryException;
import com.coverflow.Inquiry.infrastructure.InquiryRepository;
import com.coverflow.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.global.constant.Constant.NORMAL_PAGE_SIZE;

@RequiredArgsConstructor
@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    /**
     * [특정 회원의 문의 조회 메서드]
     */
    @Transactional(readOnly = true)
    public List<FindInquiryResponse> findInquiryByMemberId(
            final int pageNo,
            final String criterion,
            final String memberId
    ) {
        Pageable pageable = PageRequest.of(pageNo, NORMAL_PAGE_SIZE, Sort.by(criterion).descending());
        Page<Inquiry> inquiries = inquiryRepository.findAllByMemberIdAndStatus(pageable, memberId)
                .orElseThrow(() -> new InquiryException.InquiryNotFoundException(memberId));

        return inquiries.getContent().stream()
                .map(FindInquiryResponse::from)
                .toList();
    }

    /**
     * [관리자 전용: 전체 문의 조회 메서드]
     */
    @Transactional(readOnly = true)
    public List<FindAllInquiriesResponse> findInquiries(
            final int pageNo,
            final String criterion
    ) {
        Pageable pageable = PageRequest.of(pageNo, LARGE_PAGE_SIZE, Sort.by(criterion).descending());
        Page<Inquiry> inquiries = inquiryRepository.findInquiries(pageable)
                .orElseThrow(InquiryException.InquiryNotFoundException::new);

        return inquiries.getContent().stream()
                .map(FindAllInquiriesResponse::from)
                .toList();
    }

    /**
     * [관리자 전용: 특정 상태 문의 조회 메서드]
     * 특정 상태(답변대기/답변완료/삭제)의 회사를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public List<FindAllInquiriesResponse> findInquiriesByStatus(
            final int pageNo,
            final String criterion,
            final String status
    ) {
        Pageable pageable = PageRequest.of(pageNo, LARGE_PAGE_SIZE, Sort.by(criterion).descending());
        Page<Inquiry> inquiries = inquiryRepository.findAllByStatus(pageable, status)
                .orElseThrow(() -> new InquiryException.InquiryNotFoundException(status));

        return inquiries.getContent().stream()
                .map(FindAllInquiriesResponse::from)
                .toList();
    }

    /**
     * [문의 등록 메서드]
     */
    @Transactional
    public void saveInquiry(
            final SaveInquiryRequest request,
            final String memberId
    ) {
        Inquiry inquiry = Inquiry.builder()
                .title(request.title())
                .content(request.content())
                .status("답변대기")
                .member(Member.builder()
                        .id(UUID.fromString(memberId))
                        .build())
                .build();

        inquiryRepository.save(inquiry);
    }

    /**
     * [관리자 전용: 문의 수정 메서드]
     */
    @Transactional
    public void updateInquiry(
            final UpdateInquiryRequest request
    ) {
        Inquiry inquiry = inquiryRepository.findById(request.inquiryId())
                .orElseThrow(() -> new InquiryException.InquiryNotFoundException(request.inquiryId()));

        inquiry.updateAnswer(request.inquiryAnswer());
        inquiry.updateStatus("답변완료");
    }

    /**
     * [관리자 전용: 문의 삭제 메서드]
     */
    @Transactional
    public void deleteInquiry(final long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryException.InquiryNotFoundException(inquiryId));

        inquiry.updateStatus("삭제");
    }
}
