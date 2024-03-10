package com.coverflow.global.handler;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ResponseHandler<T>(
        HttpStatus statusCode,
        T data
) {
}
