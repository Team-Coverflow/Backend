package com.coverflow.board.presentation;

import com.coverflow.board.application.QuestionService;
import com.coverflow.board.dto.request.QuestionRequest;
import com.coverflow.board.dto.response.QuestionResponse;
import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/question")
@RestController
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/find-question")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<QuestionResponse>> findQuestionById(
            final @RequestParam("id") @Valid long id
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<QuestionResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 질문 글 조회에 성공했습니다.")
                        .data(questionService.findQuestionById(id))
                        .build()
                );
    }

    @GetMapping("/find-all-questions")
    public ResponseEntity<ResponseHandler<List<QuestionResponse>>> findAllQuestionsByCompanyId(
            final @RequestParam("id") @Valid long id
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<QuestionResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 회사의 전체 질문 글 조회에 성공했습니다.")
                        .data(questionService.findAllQuestionsByCompanyId(id))
                        .build()
                );
    }

    @GetMapping("/all-questions")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<QuestionResponse>>> findAllQuestions() {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<QuestionResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("전체 질문 글 조회에 성공했습니다.")
                        .data(questionService.findAllQuestions())
                        .build()
                );
    }

    @PostMapping("/save-question")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<QuestionResponse>> saveQuestion(
            final @RequestBody @Valid QuestionRequest questionRequest,
            final @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<QuestionResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("질문 글 등록에 성공했습니다.")
                        .data(questionService.saveQuestion(questionRequest, userDetails.getUsername()))
                        .build());
    }

    @PostMapping("/update-question")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<QuestionResponse>> updateQuestion(
            final @RequestBody @Valid QuestionRequest questionRequest,
            final @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<QuestionResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("질문 글 삭제에 성공했습니다.")
                        .data(questionService.updateQuestion(questionRequest, userDetails.getUsername()))
                        .build());
    }

    @PostMapping("/delete-question")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteQuestion(
            final @RequestBody @Valid QuestionRequest questionRequest,
            final @AuthenticationPrincipal UserDetails userDetails
    ) {
        questionService.deleteQuestion(questionRequest, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("질문 글 삭제에 성공했습니다.")
                        .build());
    }
}
