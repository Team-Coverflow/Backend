package com.coverflow.member.presentation;

import com.coverflow.member.application.MemberService;
import com.coverflow.member.dto.request.DuplicationNicknameRequest;
import com.coverflow.member.dto.response.DuplicationNicknameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/verify-duplication-nickname")
    public ResponseEntity<DuplicationNicknameResponse> verifyDuplicationNickname(@RequestBody final DuplicationNicknameRequest request) {
        final DuplicationNicknameResponse duplicationNicknameResponse = memberService.verifyDuplicationNickname(request);
        return ResponseEntity.ok(duplicationNicknameResponse);
    }
}
