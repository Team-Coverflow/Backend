package com.coverflow.company.dto.response;

import com.coverflow.company.dto.CompaniesDTO;

import java.util.List;

public record FindCompanyAdminResponse(
        List<CompaniesDTO> companies
) {

    public static FindCompanyAdminResponse from(final List<CompaniesDTO> companies) {
        return new FindCompanyAdminResponse(companies);
    }
}

