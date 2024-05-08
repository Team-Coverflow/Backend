package com.coverflow.company.dto;

import com.coverflow.company.domain.Company;
import com.coverflow.company.domain.CompanyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompaniesDTO {
    private long companyId;
    private String companyName;
    private String companyType;
    private String companyCity;
    private String companyDistrict;
    private int questionCount;
    private CompanyStatus companyStatus;

    public static CompaniesDTO from(final Company company) {
        return new CompaniesDTO(
                company.getId(),
                company.getName(),
                company.getType(),
                company.getCity(),
                company.getDistrict(),
                company.getQuestions().size(),
                company.getCompanyStatus()
        );
    }
}
