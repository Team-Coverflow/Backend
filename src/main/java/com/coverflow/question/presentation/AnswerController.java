package com.coverflow.question.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.question.application.AnswerService;
import com.coverflow.question.domain.AnswerStatus;
import com.coverflow.question.dto.AnswerDTO;
import com.coverflow.question.dto.request.SaveAnswerRequest;
import com.coverflow.question.dto.request.UpdateAnswerRequest;
import com.coverflow.question.dto.request.UpdateSelectionRequest;
import com.coverflow.question.dto.response.FindAnswerResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/answer")
@RestController
public class AnswerController {

    private final AnswerService answerService;

    /**
     * 일단 보류
     */
    @GetMapping("/answers/{questionId}")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<List<AnswerDTO>>> findAnswer(
            @RequestParam @Positive final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @PathVariable @Positive final long questionId
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<AnswerDTO>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(answerService.findAllAnswersByQuestionId(pageNo, criterion, questionId))
                        .build()
                );
    }

    @GetMapping("/admin/answers")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindAnswerResponse>>> findAllAnswers(
            @RequestParam @Positive final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAnswerResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(answerService.findAllAnswers(pageNo, criterion))
                        .build()
                );
    }

    @GetMapping("/admin/status")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindAnswerResponse>>> findAnswersByStatus(
            @RequestParam @Positive final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @RequestParam @NotBlank final AnswerStatus answerStatus
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAnswerResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(answerService.findAnswersByStatus(pageNo, criterion, answerStatus))
                        .build()
                );
    }

    @PostMapping("/")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> saveAnswer(
            @RequestBody @Valid final SaveAnswerRequest saveAnswerRequest,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        answerService.saveAnswer(saveAnswerRequest, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.CREATED)
                        .build());
    }

    @PutMapping("/selection")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> chooseAnswer(
            @RequestBody @Valid final UpdateSelectionRequest updateSelectionRequest
    ) {
        answerService.chooseAnswer(updateSelectionRequest);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }

    @PutMapping("/admin")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> updateAnswer(
            @RequestBody @Valid final UpdateAnswerRequest updateAnswerRequest
    ) {
        answerService.updateAnswer(updateAnswerRequest);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }

    @DeleteMapping("/admin/{answerId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteAnswer(
            @PathVariable @Positive final long answerId
    ) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }
}
