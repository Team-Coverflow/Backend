package com.coverflow.question.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.question.application.QuestionService;
import com.coverflow.question.domain.QuestionStatus;
import com.coverflow.question.dto.request.SaveQuestionRequest;
import com.coverflow.question.dto.request.UpdateQuestionRequest;
import com.coverflow.question.dto.response.FindAllQuestionsResponse;
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

@RequiredArgsConstructor
@RequestMapping("/api/question")
@RestController
public class QuestionController {

    private final QuestionService questionService;

    /**
     * 일단 보류
     */
//    @GetMapping("/questions/{companyId}")
//    public ResponseEntity<ResponseHandler<List<QuestionDTO>>> findAllQuestionsByCompanyId(
//            @RequestParam @PositiveOrZero final int pageNo,
//            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
//            @PathVariable @Positive final long companyId
//    ) {
//        return ResponseEntity.ok()
//                .body(ResponseHandler.<List<QuestionDTO>>builder()
//                        .statusCode(HttpStatus.OK)
//                        .data(questionService.findAllQuestionsByCompanyId(pageNo, criterion, companyId))
//                        .build()
//                );
//    }
    @GetMapping("/{questionId}")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<FindQuestionResponse>> findQuestionById(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @PathVariable @Positive final long questionId
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindQuestionResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(questionService.findQuestionById(pageNo, criterion, questionId))
                        .build()
                );
    }

    @GetMapping("/admin")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<FindAllQuestionsResponse>> findAllQuestions(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindAllQuestionsResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(questionService.findAllQuestions(pageNo, criterion))
                        .build()
                );
    }

    @GetMapping("/admin/status")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<FindAllQuestionsResponse>> findQuestionsByStatus(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @RequestParam @NotBlank final QuestionStatus questionStatus
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindAllQuestionsResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(questionService.findQuestionsByStatus(pageNo, criterion, questionStatus))
                        .build()
                );
    }

    @PostMapping
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> saveQuestion(
            @RequestBody @Valid final SaveQuestionRequest saveQuestionRequest,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        questionService.saveQuestion(saveQuestionRequest, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.CREATED)
                        .build());
    }

    @PutMapping
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> updateQuestion(
            @RequestBody @Valid final UpdateQuestionRequest updateQuestionRequest
    ) {
        questionService.updateQuestion(updateQuestionRequest);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }

    @DeleteMapping("/admin/{questionId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteQuestion(
            @PathVariable @Positive final long questionId
    ) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }
}
