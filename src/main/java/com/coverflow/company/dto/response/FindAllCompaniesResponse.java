package com.coverflow.company.dto.response;

import com.coverflow.company.dto.CompaniesDTO;

import java.util.List;

public record FindAllCompaniesResponse(
        int totalPages,
        long totalCompanyCount,
        List<CompaniesDTO> companies
) {

    public static FindAllCompaniesResponse of(
            final int totalPages,
            final long totalCompanyCount,
            final List<CompaniesDTO> companies
    ) {
        return new FindAllCompaniesResponse(totalPages, totalCompanyCount, companies);
    }
}

