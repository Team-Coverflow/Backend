package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;
import com.coverflow.company.domain.CompanyStatus;

public record SearchCompanyResponse(
        long companyId,
        String companyName,
        String companyType,
        String companyAddress,
        String companyEstablishment,
        int questionCount,
        CompanyStatus companyStatus
) {

    public static SearchCompanyResponse from(final Company company) {
        return new SearchCompanyResponse(
                company.getId(),
                company.getName(),
                company.getType(),
                company.getCity() + " " + company.getDistrict(),
                company.getEstablishment(),
                company.getQuestionCount(),
                company.getCompanyStatus()
        );
    }
}
