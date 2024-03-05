package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;
import com.coverflow.company.domain.CompanyStatus;

public record FindAutoCompleteResponse(
        long companyId,
        String companyName,
        String companyType,
        String companyAddress,
        String companyEstablishment,
        int questionCount,
        CompanyStatus companyStatus
) {

    public static FindAutoCompleteResponse from(final Company company) {
        return new FindAutoCompleteResponse(
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
