package com.coverflow.company.infrastructure;

import com.coverflow.company.domain.Company;
import com.coverflow.company.dto.request.FindCompanyAdminRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompanyRepositoryCustom {

    Optional<Page<Company>> findWithFilters(final Pageable pageable, final FindCompanyAdminRequest companyStatus);
}
