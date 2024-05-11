package com.coverflow.global.oauth2.dto;

public record TokenRequest(
        String code,
        String agreeMarket,
        String agreeCollection
) {
}
