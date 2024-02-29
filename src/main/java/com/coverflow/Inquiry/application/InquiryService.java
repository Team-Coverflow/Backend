package com.coverflow.Inquiry.application;

import com.coverflow.Inquiry.domain.Inquiry;
import com.coverflow.Inquiry.dto.request.SaveInquiryRequest;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        final Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(criterion).descending());
        final Page<Inquiry> inquiries = inquiryRepository.findAllByMemberIdAndStatus(pageable, memberId)
                .orElseThrow(() -> new InquiryException.InquiryNotFoundException(memberId));
        final List<FindInquiryResponse> findInquiries = new ArrayList<>();

        for (int i = 0; i < inquiries.getContent().size(); i++) {
            findInquiries.add(i, FindInquiryResponse.from(inquiries.getContent().get(i)));
        }
        return findInquiries;
    }

    /**
     * [관리자 전용: 전체 문의 조회 메서드]
     */
    @Transactional(readOnly = true)
    public List<FindAllInquiriesResponse> findInquiries(
            final int pageNo,
            final String criterion
    ) {
        final Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(criterion).descending());
        final Page<Inquiry> inquiries = inquiryRepository.findInquiries(pageable)
                .orElseThrow(InquiryException.InquiryNotFoundException::new);
        final List<FindAllInquiriesResponse> findInquiries = new ArrayList<>();

        for (int i = 0; i < inquiries.getContent().size(); i++) {
            findInquiries.add(i, FindAllInquiriesResponse.from(inquiries.getContent().get(i)));
        }
        return findInquiries;
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
        final Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(criterion).descending());
        final Page<Inquiry> inquiries = inquiryRepository.findAllByStatus(pageable, status)
                .orElseThrow(() -> new InquiryException.InquiryNotFoundException(status));
        final List<FindAllInquiriesResponse> findInquiries = new ArrayList<>();

        for (int i = 0; i < inquiries.getContent().size(); i++) {
            findInquiries.add(i, FindAllInquiriesResponse.from(inquiries.getContent().get(i)));
        }
        return findInquiries;
    }

    /**
     * [관리자 전용: 문의 등록 메서드]
     */
    @Transactional
    public void saveInquiry(
            final SaveInquiryRequest request,
            final String memberId
    ) {
        Inquiry inquiry = Inquiry.builder()
                .content(request.content())
                .status("등록")
                .member(Member.builder()
                        .id(UUID.fromString(memberId))
                        .build())
                .build();

        inquiryRepository.save(inquiry);
    }

    /**
     * [관리자 전용: 문의 삭제 메서드]
     */
    @Transactional
    public void deleteInquiry(final Long inquiryId) {
        final Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryException.InquiryNotFoundException(inquiryId));

        inquiry.updateStatus("삭제");
    }
}
