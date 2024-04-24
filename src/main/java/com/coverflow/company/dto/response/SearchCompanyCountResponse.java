package com.coverflow.company.dto.response;

public record SearchCompanyCountResponse(
        int totalPages,
        long totalElements
) {
    public static SearchCompanyCountResponse of(
            final int totalPages,
            final long totalElements
    ) {
        return new SearchCompanyCountResponse(totalPages, totalElements);
    }
}
