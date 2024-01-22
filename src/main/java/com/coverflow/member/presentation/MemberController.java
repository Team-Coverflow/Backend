package com.coverflow.member.presentation;

import com.coverflow.member.application.MemberService;
import com.coverflow.member.dto.request.DuplicationNicknameRequest;
import com.coverflow.member.dto.request.LoginInfoRequest;
import com.coverflow.member.dto.response.DuplicationNicknameResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/verify-duplication-nickname")
    public ResponseEntity<DuplicationNicknameResponse> verifyDuplicationNickname(
            @RequestBody @Valid final DuplicationNicknameRequest request
    ) {
        DuplicationNicknameResponse duplicationNicknameResponse = memberService.verifyDuplicationNickname(request);
        return ResponseEntity.ok().body(duplicationNicknameResponse);
    }

    @PostMapping("/save-login-info")
    public ResponseEntity<Void> saveLoginInfo(
            @RequestBody @Valid final LoginInfoRequest request
    ) {
        memberService.saveLoginInfo(request);
        return ResponseEntity.ok().build();
    }
}
