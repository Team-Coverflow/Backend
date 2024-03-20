package com.coverflow.company.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCompanyRequest(

        @NotBlank
        String name,
        @NotBlank
        String type,
        @NotBlank
        String city,
        @NotBlank
        String district,
        @NotBlank
        String establishment,
        @NotBlank
        String companyStatus
) {
}
