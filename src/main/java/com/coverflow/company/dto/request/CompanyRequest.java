package com.coverflow.company.dto.request;

public record CompanyRequest(
        String name,
        String type,
        String city,
        String district,
        String establishment
) {
}
