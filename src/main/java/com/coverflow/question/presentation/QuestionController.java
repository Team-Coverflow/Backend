package com.coverflow.question.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.question.application.QuestionService;
import com.coverflow.question.dto.request.DeleteQuestionRequest;
import com.coverflow.question.dto.request.SaveQuestionRequest;
import com.coverflow.question.dto.request.UpdateQuestionRequest;
import com.coverflow.question.dto.response.FindQuestionResponse;
import com.coverflow.question.dto.response.QuestionResponse;
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

    @GetMapping("/find-questions")
    public ResponseEntity<ResponseHandler<List<QuestionResponse>>> findAllQuestionsByCompanyId(
            final @RequestParam("id") @Valid Long id
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<QuestionResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 회사의 전체 질문 조회에 성공했습니다.")
                        .data(questionService.findAllQuestionsByCompanyId(id))
                        .build()
                );
    }

    @GetMapping("/find-question")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<FindQuestionResponse>> findQuestionById(
            final @RequestParam("id") @Valid Long id
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindQuestionResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 질문 조회에 성공했습니다.")
                        .data(questionService.findQuestionById(id))
                        .build()
                );
    }

    @GetMapping("/admin/find-questions")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<QuestionResponse>>> findAllQuestions() {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<QuestionResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("전체 질문 조회에 성공했습니다.")
                        .data(questionService.findAllQuestions())
                        .build()
                );
    }

    @PostMapping("/save-question")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> saveQuestion(
            final @RequestBody @Valid SaveQuestionRequest saveQuestionRequest,
            final @AuthenticationPrincipal UserDetails userDetails
    ) {
        questionService.saveQuestion(saveQuestionRequest, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("질문 등록에 성공했습니다.")
                        .build());
    }

    @PostMapping("/admin/update-question")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> updateQuestion(
            final @RequestBody @Valid UpdateQuestionRequest updateQuestionRequest
    ) {
        questionService.updateQuestion(updateQuestionRequest);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("질문 수정에 성공했습니다.")
                        .build());
    }

    @PostMapping("/admin/delete-question")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteQuestion(
            final @RequestBody @Valid DeleteQuestionRequest deleteAnswerRequest
    ) {
        questionService.deleteQuestion(deleteAnswerRequest);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("질문 삭제에 성공했습니다.")
                        .build());
    }
}
