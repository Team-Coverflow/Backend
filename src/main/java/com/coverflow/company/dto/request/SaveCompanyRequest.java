package com.coverflow.company.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SaveCompanyRequest(

        @NotBlank
        String name,
        @NotBlank
        String type,
        @NotBlank
        String city,
        @NotBlank
        String district
) {
}
