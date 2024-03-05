package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;

public record FindAllCompaniesResponse(
        long companyId,
        String companyName,
        String companyType,
        String companyCity,
        String companyDistrict,
        String companyEstablishment,
        int questionCount,
        String companyStatus
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
