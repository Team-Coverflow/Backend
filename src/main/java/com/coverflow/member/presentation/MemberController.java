package com.coverflow.member.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.member.application.MemberService;
import com.coverflow.member.domain.MemberStatus;
import com.coverflow.member.dto.request.SaveMemberRequest;
import com.coverflow.member.dto.response.FindAllMembersResponse;
import com.coverflow.member.dto.response.FindMemberInfoResponse;
import com.coverflow.member.dto.response.UpdateNicknameResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
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

    @GetMapping
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<FindMemberInfoResponse>> findMemberById(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindMemberInfoResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(memberService.findMemberById(userDetails.getUsername()))
                        .build());
    }

    @GetMapping("/admin")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<FindAllMembersResponse>> findAllMemberById(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindAllMembersResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(memberService.findAllMembers(pageNo, criterion))
                        .build());
    }

    @GetMapping("/admin/status")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<FindAllMembersResponse>> findMembersByStatus(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @RequestParam @NotBlank final MemberStatus memberStatus
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindAllMembersResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(memberService.findMembersByStatus(pageNo, criterion, memberStatus))
                        .build()
                );
    }

    @PostMapping
    public ResponseEntity<ResponseHandler<Void>> saveMember(
            @AuthenticationPrincipal final UserDetails userDetails,
            @RequestBody final SaveMemberRequest request
    ) {
        memberService.saveMemberInfo(userDetails.getUsername(), request);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.CREATED)
                        .build());
    }

    @PutMapping
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<UpdateNicknameResponse>> updateNickname(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<UpdateNicknameResponse>builder()
                        .statusCode(HttpStatus.RESET_CONTENT)
                        .data(memberService.updateNickname(userDetails.getUsername()))
                        .build());
    }

    @PutMapping("/logout")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> logout(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        memberService.logout(userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }

    @DeleteMapping("/leave")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteMember(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        memberService.suspend(userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }
}
