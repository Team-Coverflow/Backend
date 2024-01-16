package com.coverflow.member.presentation;

import com.coverflow.member.application.MemberService;
import com.coverflow.member.dto.MemberSignUpDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/sign-up")
    public String signUp(@RequestBody MemberSignUpDTO memberSignUpDto) throws Exception {
        memberService.signUp(memberSignUpDto);
        return "회원가입 성공";
    }

    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }
}
