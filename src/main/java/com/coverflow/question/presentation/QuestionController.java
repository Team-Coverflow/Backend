package com.coverflow.question.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.global.util.BadWordUtil;
import com.coverflow.question.application.QuestionService;
import com.coverflow.question.dto.request.FindQuestionAdminRequest;
import com.coverflow.question.dto.request.SaveQuestionRequest;
import com.coverflow.question.dto.request.UpdateQuestionRequest;
import com.coverflow.question.dto.response.FindAllQuestionsResponse;
import com.coverflow.question.dto.response.FindMyQuestionsResponse;
import com.coverflow.question.dto.response.FindQuestionResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/question")
@RestController
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/me")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<FindMyQuestionsResponse>> findMyQuestions(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindMyQuestionsResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(questionService.findByMemberId(pageNo, criterion, UUID.fromString(userDetails.getUsername())))
                        .build()
                );
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<ResponseHandler<FindQuestionResponse>> findById(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @PathVariable @Positive final long questionId
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindQuestionResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(questionService.findById(pageNo, criterion, questionId))
                        .build()
                );
    }

    @GetMapping("/admin")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<FindAllQuestionsResponse>> find(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @ModelAttribute final FindQuestionAdminRequest request
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindAllQuestionsResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(questionService.find(pageNo, criterion, request))
                        .build()
                );
    }

    @PostMapping
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> save(
            @RequestBody @Valid final SaveQuestionRequest request,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        BadWordUtil.check(request.title());
        BadWordUtil.check(request.content());
        questionService.save(request, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.CREATED)
                        .build());
    }

    @PatchMapping("/{questionId}")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> update(
            @PathVariable @Positive final long questionId,
            @RequestBody @Valid final UpdateQuestionRequest request
    ) {
        BadWordUtil.check(request.title());
        BadWordUtil.check(request.content());
        questionService.update(questionId, request);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }

    @DeleteMapping("/{questionId}")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> delete(
            @PathVariable @Positive final long questionId
    ) {
        questionService.delete(questionId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }
}
