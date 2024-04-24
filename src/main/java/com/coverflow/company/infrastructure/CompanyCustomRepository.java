package com.coverflow.company.infrastructure;

import com.coverflow.company.domain.Company;
import com.coverflow.company.dto.request.FindCompanyAdminRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface CompanyCustomRepository {

    Optional<Slice<Company>> findByNameStartingWith(final Pageable pageable, final String name);

    Long countByName(final String name);

    Optional<Page<Company>> findWithFilters(final Pageable pageable, final FindCompanyAdminRequest request);
}
