package com.coverflow.visitor.dto.response;

import com.coverflow.visitor.domain.Visitor;

import java.time.LocalDate;

public record FindDailyVisitorResponse(
        int count,
        LocalDate createdAt
) {
    public static FindDailyVisitorResponse from(final Visitor visitor) {
        return new FindDailyVisitorResponse(
                visitor.getCount(),
                visitor.getCreatedAt().toLocalDate()
        );
    }
}
