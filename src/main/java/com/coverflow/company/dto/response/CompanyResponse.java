package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;

public record CompanyResponse(
        String name,
        String type,
        String address,
        String establishment
) {
    public static CompanyResponse of(Company company) {
        return new CompanyResponse(
                company.getName(),
                company.getType(),
                company.getCity() + " " + company.getDistrict(),
                company.getEstablishment()
        );
    }
}
