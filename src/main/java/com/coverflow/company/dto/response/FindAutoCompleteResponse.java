package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;

public record FindAutoCompleteResponse(
        Long id,
        String name,
        String type,
        String address,
        String establishment,
        int questionCount,
        String status
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
