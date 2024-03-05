package com.coverflow.company.dto.request;

public record UpdateCompanyRequest(
        long companyId,
        String name,
        String type,
        String city,
        String district,
        String establishment,
        String status
) {
}
