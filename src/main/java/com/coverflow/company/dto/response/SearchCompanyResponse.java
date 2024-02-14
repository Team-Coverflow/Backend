package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;

public record SearchCompanyResponse(
        Long id,
        String name,
        String type,
        String address,
        String establishment,
        int questionCount,
        String status
) {

    public static SearchCompanyResponse from(final Company company) {
        return new SearchCompanyResponse(
                company.getId(),
                company.getName(),
                company.getType(),
                company.getCity() + " " + company.getDistrict(),
                company.getEstablishment(),
                company.getQuestionCount(),
                company.getStatus()
        );
    }
}
