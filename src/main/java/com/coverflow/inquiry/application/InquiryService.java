package com.coverflow.inquiry.application;

import com.coverflow.inquiry.dto.request.FindInquiryAdminRequest;
import com.coverflow.inquiry.dto.request.SaveInquiryRequest;
import com.coverflow.inquiry.dto.request.UpdateInquiryRequest;
import com.coverflow.inquiry.dto.response.FindAllInquiriesResponse;
import com.coverflow.inquiry.dto.response.FindInquiryResponse;

public interface InquiryService {

    /**
     * [특정 회원의 문의 조회 메서드]
     */
    FindInquiryResponse findMyInquiries(
            final int pageNo,
            final String criterion,
            final String memberId
    );

    /**
     * [관리자 - 문의 조회 메서드]
     */
    FindAllInquiriesResponse find(
            final int pageNo,
            final String criterion,
            final FindInquiryAdminRequest request
    );

    /**
     * [문의 등록 메서드]
     */
    void save(final SaveInquiryRequest request, final String memberId);

    /**
     * [관리자 - 문의 수정 메서드]
     */
    void update(final long inquiryId, final UpdateInquiryRequest request);

    /**
     * [관리자 - 문의 삭제 메서드]
     */
    void delete(final long inquiryId);
}
