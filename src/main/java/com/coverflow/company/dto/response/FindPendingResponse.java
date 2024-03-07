package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;
import com.coverflow.company.domain.CompanyStatus;

public record FindPendingResponse(
        long id,
        String name,
        String type,
        String address,
        String establishment,
        int questionCount,
        CompanyStatus companyStatus
) {

    public static FindPendingResponse from(final Company company) {
        return new FindPendingResponse(
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
