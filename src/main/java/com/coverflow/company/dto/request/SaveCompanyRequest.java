package com.coverflow.company.dto.request;

public record SaveCompanyRequest(
        long companyId,
        String name,
        String type,
        String city,
        String district,
        String establishment
) {
}
