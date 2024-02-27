package com.coverflow.enquiry.application;

import com.coverflow.enquiry.domain.Enquiry;
import com.coverflow.enquiry.dto.request.SaveEnquiryRequest;
import com.coverflow.enquiry.dto.response.FindEnquiryResponse;
import com.coverflow.enquiry.exception.EnquiryException;
import com.coverflow.enquiry.infrastructure.EnquiryRepository;
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

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;

    /**
     * [특정 회원의 문의 조회 메서드]
     */
    public List<FindEnquiryResponse> findEnquiryByMemberId(
            final UUID memberId,
            final int pageNo,
            final String criterion
    ) {
        final Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(criterion).descending());
        final Page<Enquiry> enquiries = enquiryRepository.findAllByMemberIdAndStatus(memberId, "등록", pageable)
                .orElseThrow(() -> new EnquiryException.EnquiryNotFoundException(memberId));
        final List<FindEnquiryResponse> findEnquiries = new ArrayList<>();

        for (int i = 0; i < enquiries.getContent().size(); i++) {
            findEnquiries.add(i, FindEnquiryResponse.from(enquiries.getContent().get(i)));
        }
        return findEnquiries;
    }

    /**
     * [관리자 전용: 전체 문의 조회 메서드]
     */
    public List<FindEnquiryResponse> findEnquiries(
            final int pageNo,
            final String criterion
    ) {
        final Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(criterion).descending());
        final Page<Enquiry> enquiries = enquiryRepository.findEnquiries(pageable)
                .orElseThrow(EnquiryException.EnquiryNotFoundException::new);
        final List<FindEnquiryResponse> findEnquiries = new ArrayList<>();

        for (int i = 0; i < enquiries.getContent().size(); i++) {
            findEnquiries.add(i, FindEnquiryResponse.from(enquiries.getContent().get(i)));
        }
        return findEnquiries;
    }

    /**
     * [관리자 전용: 문의 등록 메서드]
     */
    public void saveEnquiry(
            final SaveEnquiryRequest request,
            final String memberId
    ) {
        Enquiry enquiry = Enquiry.builder()
                .content(request.content())
                .status("등록")
                .member(Member.builder()
                        .id(UUID.fromString(memberId))
                        .build())
                .build();

        enquiryRepository.save(enquiry);
    }

    /**
     * [관리자 전용: 문의 삭제 메서드]
     */
    public void deleteEnquiry(final Long enquiryId) {
        final Enquiry enquiry = enquiryRepository.findById(enquiryId)
                .orElseThrow(() -> new EnquiryException.EnquiryNotFoundException(enquiryId));

        enquiry.updateStatus("삭제");
    }
}
