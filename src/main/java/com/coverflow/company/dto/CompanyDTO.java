package com.coverflow.company.dto;

import com.coverflow.company.domain.Company;
import com.coverflow.company.domain.CompanyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    private long companyId;
    private String companyName;
    private String companyType;
    private String companyAddress;
    private int questionCount;
    private CompanyStatus companyStatus;

    public static CompanyDTO from(final Company company) {
        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getType(),
                company.getCity() + " " + company.getDistrict(),
                company.getQuestions().size(),
                company.getCompanyStatus()
        );
    }
}
