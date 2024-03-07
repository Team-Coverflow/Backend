package com.coverflow.company.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record SaveCompanyRequest(

        @Positive
        long companyId,
        @NotBlank
        String name,
        @NotBlank
        String type,
        @NotBlank
        String city,
        @NotBlank
        String district,
        String establishment
) {
}
