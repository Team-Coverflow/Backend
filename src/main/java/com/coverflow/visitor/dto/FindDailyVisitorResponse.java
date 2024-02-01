package com.coverflow.visitor.dto;

import com.coverflow.visitor.domain.Visitor;

public record FindDailyVisitorResponse(
        int count
) {
    public static FindDailyVisitorResponse of(Visitor visitor) {
        return new FindDailyVisitorResponse(visitor.getCount());
    }
}
