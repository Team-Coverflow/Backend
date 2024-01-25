package com.coverflow.member.presentation;

import com.coverflow.member.application.MemberService;
import com.coverflow.member.dto.request.MemberSaveMemberInfoRequest;
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

    @PostMapping("/save-member-info")
    public ResponseEntity<Void> saveMemberInfo(
            @AuthenticationPrincipal final UserDetails userDetails,
            @RequestBody @Valid final MemberSaveMemberInfoRequest request
    ) {
        memberService.saveMemberInfo(userDetails.getUsername(), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-nickname")
    public ResponseEntity<Void> updateNickname(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        memberService.updateNickname(userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        memberService.logout(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/leave")
    public ResponseEntity<Void> deleteMember(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        memberService.leaveMember(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}