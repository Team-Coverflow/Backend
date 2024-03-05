package com.coverflow.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("GUEST"),
    MEMBER("MEMBER"),
    PREMIUM("PREMIUM"),
    ADMIN("ADMIN");

    private final String key;
}

