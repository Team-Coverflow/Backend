package com.coverflow.inquiry.infrastructure;

import com.coverflow.inquiry.domain.Inquiry;
import com.coverflow.inquiry.dto.request.FindInquiryAdminRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InquiryCustomRepository {

    Optional<Page<Inquiry>> findWithFilters(final Pageable pageable, final FindInquiryAdminRequest request);
}
