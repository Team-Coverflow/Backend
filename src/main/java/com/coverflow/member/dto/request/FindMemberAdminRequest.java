package com.coverflow.member.dto.request;

public record FindMemberAdminRequest(
        String createdStartDate,
        String createdEndDate,
        String status,
        String role,
        String connectedStartDate,
        String connectedEndDate
) {
}
