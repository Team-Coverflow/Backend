package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;

public record FindPendingResponse(
        long id,
        String name,
        String type,
        String address,
        String establishment,
        int questionCount,
        String status
) {

    public static FindPendingResponse from(final Company company) {
        return new FindPendingResponse(
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
