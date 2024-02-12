package com.coverflow.company.dto.request;

public record SaveCompanyRequest(
        Long companyId,
        String name,
        String type,
        String city,
        String district,
        String establishment
) {
}
