package com.coverflow.member.presentation;

import com.coverflow.member.application.MemberService;
import com.coverflow.member.dto.request.MemberSaveMemberInfoRequest;
import com.coverflow.member.dto.request.MemberVerifyDuplicationNicknameRequest;
import com.coverflow.member.dto.response.MemberVerifyDuplicationNicknameResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/verify-duplication-nickname")
    public ResponseEntity<MemberVerifyDuplicationNicknameResponse> verifyDuplicationNickname(
            @RequestBody @Valid final MemberVerifyDuplicationNicknameRequest request
    ) {
        MemberVerifyDuplicationNicknameResponse duplicationNicknameResponse = memberService.verifyDuplicationNickname(request);
        return ResponseEntity.ok().body(duplicationNicknameResponse);
    }

    @PostMapping("/save-member-info")
    public ResponseEntity<Void> saveMemberInfo(
            @AuthenticationPrincipal final UserDetails userDetails,
            @RequestBody @Valid final MemberSaveMemberInfoRequest request
    ) {
        memberService.saveMemberInfo(userDetails.getUsername(), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/leave")
    public ResponseEntity<Void> deleteMember(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        memberService.leaveMember(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
