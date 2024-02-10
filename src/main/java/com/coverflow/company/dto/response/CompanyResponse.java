package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;

public record CompanyResponse(
        Long id,
        String name,
        String type,
        String address,
        String establishment
) {
    public static CompanyResponse from(final Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getType(),
                company.getCity() + " " + company.getDistrict(),
                company.getEstablishment()
        );
    }
}
