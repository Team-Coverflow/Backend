package com.coverflow.company.dto.response;

import com.coverflow.company.dto.CompanyDTO;

import java.util.List;

public record SearchCompanyResponse(

        List<CompanyDTO> companyList
) {

    public static SearchCompanyResponse from(final List<CompanyDTO> companyList) {
        return new SearchCompanyResponse(companyList);
    }
}
