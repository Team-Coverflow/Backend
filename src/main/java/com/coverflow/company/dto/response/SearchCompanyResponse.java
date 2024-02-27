package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;

public record SearchCompanyResponse(
        Long companyId,
        String companyName,
        String companyType,
        String companyAddress,
        String companyEstablishment,
        int questionCount,
        String companyStatus
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
