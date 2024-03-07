package com.coverflow.global.oauth2.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/login/oauth2/code")
@RestController
public class OAuth2LoginController {

    @GetMapping("/kakao")
    public void code(
            @RequestParam final String code
    ) {
        System.out.println("code = " + code);
    }

}
