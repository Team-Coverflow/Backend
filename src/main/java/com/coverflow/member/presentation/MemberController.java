package com.coverflow.member.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.response.ResponseHandler;
import com.coverflow.member.application.MemberService;
import com.coverflow.member.dto.request.SaveMemberInfoRequest;
import com.coverflow.member.dto.response.FindMemberInfoResponse;
import com.coverflow.member.dto.response.UpdateNicknameResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/find-member")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<FindMemberInfoResponse>> findMemberById(
            final @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindMemberInfoResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회원 조회 성공했습니다.")
                        .data(memberService.findMemberById(userDetails.getUsername()))
                        .build());
    }

    @GetMapping("/find-all-member")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindMemberInfoResponse>>> findAllMemberById() {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindMemberInfoResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("모든 회원 조회 성공했습니다.")
                        .data(memberService.findAllMember())
                        .build());
    }

    @PostMapping("/save-member-info")
    public ResponseEntity<ResponseHandler<Void>> saveMemberInfo(
            final @AuthenticationPrincipal UserDetails userDetails,
            final @RequestBody @Valid SaveMemberInfoRequest request
    ) {
        memberService.saveMemberInfo(userDetails.getUsername(), request);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회원 정보 등록에 성공했습니다.")
                        .build());
    }

    @PostMapping("/update-nickname")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<UpdateNicknameResponse>> updateNickname(
            final @AuthenticationPrincipal UserDetails userDetails
    ) {
        memberService.updateNickname(userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<UpdateNicknameResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("닉네임 변경에 성공했습니다.")
                        .data(memberService.updateNickname(userDetails.getUsername()))
                        .build());
    }

    @GetMapping("/logout")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> logout(
            final @AuthenticationPrincipal UserDetails userDetails
    ) {
        memberService.logout(userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("로그아웃에 성공했습니다.")
                        .build());
    }

    @PostMapping("/leave")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteMember(
            final @AuthenticationPrincipal UserDetails userDetails
    ) {
        memberService.leaveMember(userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회원 탈퇴에 성공했습니다.")
                        .build());
    }
}