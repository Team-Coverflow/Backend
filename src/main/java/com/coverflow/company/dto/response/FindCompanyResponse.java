package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;

public record FindCompanyResponse(
        String name,
        String type,
        String address,
        String establishment
) {
    public static FindCompanyResponse of(Company company) {
        return new FindCompanyResponse(
                company.getName(),
                company.getType(),
                company.getCity() + " " + company.getDistrict(),
                company.getEstablishment()
        );
    }
}
