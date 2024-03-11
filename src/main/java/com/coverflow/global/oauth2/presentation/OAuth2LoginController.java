package com.coverflow.global.oauth2.presentation;

import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.global.oauth2.service.OAuth2LoginService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth/code")
@RestController
public class OAuth2LoginController {

    private final OAuth2LoginService oAuth2LoginService;

    @GetMapping
    public ResponseEntity<ResponseHandler<Void>> code(
            @RequestParam @NotBlank final String code
    ) {
        log.info("인가 코드 수신 성공 => {}", code);

        // '/'를 구분자로 사용하여 문자열을 분리
        String[] tokens = oAuth2LoginService.getToken(code).split("/");

        // 배열의 각 요소에 접근
        String extractedAccessToken = tokens[0];
        String extractedRefreshToken = tokens[1];

        return ResponseEntity.ok()
                .header("Authorization", extractedAccessToken)
                .header("Authorization-refresh", extractedRefreshToken)
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.CREATED)
                        .build());
    }

}
