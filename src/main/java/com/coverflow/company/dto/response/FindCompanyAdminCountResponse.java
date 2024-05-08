package com.coverflow.company.dto.response;

public record FindCompanyAdminCountResponse(
        int totalPages,
        long totalElements
) {
    public static FindCompanyAdminCountResponse of(
            final int totalPages,
            final long totalElements
    ) {
        return new FindCompanyAdminCountResponse(totalPages, totalElements);
    }
}
