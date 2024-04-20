package com.coverflow.member.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.member.application.MemberService;
import com.coverflow.member.dto.request.FindMemberAdminRequest;
import com.coverflow.member.dto.request.SaveMemberRequest;
import com.coverflow.member.dto.request.UpdateMemberRequest;
import com.coverflow.member.dto.response.FindAllMembersResponse;
import com.coverflow.member.dto.response.FindMemberResponse;
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

    @GetMapping("/me")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<FindMemberResponse>> findMyMember(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindMemberResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(memberService.findMyMember(userDetails.getUsername()))
                        .build());
    }

    @GetMapping("/admin")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<FindAllMembersResponse>> find(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @ModelAttribute final FindMemberAdminRequest request
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindAllMembersResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(memberService.find(pageNo, criterion, request))
                        .build());
    }

    @PostMapping
    public ResponseEntity<ResponseHandler<Void>> save(
            @AuthenticationPrincipal final UserDetails userDetails,
            @RequestBody final SaveMemberRequest request
    ) {
        memberService.save(userDetails.getUsername(), request);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.CREATED)
                        .build());
    }

    @PatchMapping
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> update(
            @AuthenticationPrincipal final UserDetails userDetails,
            @RequestBody final UpdateMemberRequest request
    ) {
        memberService.update(userDetails.getUsername(), request);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .build());
    }

    @PatchMapping("/nickname")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<UpdateNicknameResponse>> updateNickname(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<UpdateNicknameResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(memberService.updateNickname(userDetails.getUsername()))
                        .build());
    }

    @PatchMapping("/logout")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> logout(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        memberService.logout(userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .build());
    }

    @DeleteMapping("/leave")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> delete(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        memberService.delete(userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }
}
