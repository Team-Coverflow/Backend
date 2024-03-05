package com.coverflow.company.dto.request;

import com.coverflow.company.domain.CompanyStatus;

public record UpdateCompanyRequest(
        long companyId,
        String name,
        String type,
        String city,
        String district,
        String establishment,
        CompanyStatus companyStatus
) {
}
