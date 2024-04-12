package com.coverflow.company.dto.response;

import com.coverflow.company.dto.CompanyDTO;

import java.util.List;

public record SearchCompanyResponse(
        int totalPages,
        long totalElements,
        List<CompanyDTO> companyList
) {

    public static SearchCompanyResponse of(
            final int totalPages,
            final long totalElements,
            final List<CompanyDTO> companyList
    ) {
        return new SearchCompanyResponse(totalPages, totalElements, companyList);
    }
}
