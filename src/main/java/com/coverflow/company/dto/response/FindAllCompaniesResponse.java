package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;

public record FindAllCompaniesResponse(
        Long id,
        String name,
        String type,
        String city,
        String district,
        String establishment,
        int questionCount,
        String status
) {

    public static FindAllCompaniesResponse from(final Company company) {
        return new FindAllCompaniesResponse(
                company.getId(),
                company.getName(),
                company.getType(),
                company.getCity(),
                company.getDistrict(),
                company.getEstablishment(),
                company.getQuestionCount(),
                company.getStatus()
        );
    }
}
