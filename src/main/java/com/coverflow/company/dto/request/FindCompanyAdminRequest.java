package com.coverflow.company.dto.request;

public record FindCompanyAdminRequest(
        String type,
        String city,
        String district,
        String companyStatus
) {
}
