package com.coverflow.visitor.dto.response;

import com.coverflow.visitor.domain.Visitor;

public record FindDailyVisitorResponse(
        int count
) {
    public static FindDailyVisitorResponse from(Visitor visitor) {
        return new FindDailyVisitorResponse(visitor.getCount());
    }
}
