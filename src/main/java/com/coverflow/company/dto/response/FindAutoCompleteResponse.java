package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;

public record FindAutoCompleteResponse(
        long companyId,
        String companyName,
        String companyType,
        String companyAddress,
        String companyEstablishment,
        int questionCount,
        String companyStatus
) {

    public static FindAutoCompleteResponse from(final Company company) {
        return new FindAutoCompleteResponse(
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
