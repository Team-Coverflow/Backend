package com.coverflow.member.infrastructure;

import com.coverflow.company.dto.request.FindCompanyAdminRequest;
import com.coverflow.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<Page<Member>> findWithFilters(final Pageable pageable, final FindCompanyAdminRequest request);
}
