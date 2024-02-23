package com.coverflow.question.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.question.application.AnswerService;
import com.coverflow.question.dto.request.SaveAnswerRequest;
import com.coverflow.question.dto.request.UpdateAnswerRequest;
import com.coverflow.question.dto.request.UpdateSelectionRequest;
import com.coverflow.question.dto.response.FindAnswerResponse;
import jakarta.validation.Valid;
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

    @GetMapping("/find-answers/{questionId}")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<List<FindAnswerResponse>>> findAnswer(
            @PathVariable @Valid final Long questionId
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAnswerResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 질문에 대한 전체 답변 조회에 성공했습니다.")
                        .data(answerService.findAnswer(questionId))
                        .build()
                );
    }

    @GetMapping("/admin/find-answer/{answerId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<FindAnswerResponse>> findAnswerById(
            @PathVariable @Valid final Long answerId
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindAnswerResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 답변 조회에 성공했습니다.")
                        .data(answerService.findById(answerId))
                        .build()
                );
    }

    @GetMapping("/admin/find-answers")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindAnswerResponse>>> findAnswers() {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAnswerResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("전체 답변 조회에 성공했습니다.")
                        .data(answerService.findAnswers())
                        .build()
                );
    }

    @PostMapping("/save-answer")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> saveAnswer(
            @RequestBody @Valid final SaveAnswerRequest saveAnswerRequest,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        answerService.saveAnswer(saveAnswerRequest, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("답변 등록에 성공했습니다.")
                        .build());
    }

    @PostMapping("/update-selection")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> chooseAnswer(
            @RequestBody @Valid final UpdateSelectionRequest updateSelectionRequest
    ) {
        answerService.chooseAnswer(updateSelectionRequest);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("답변 채택에 성공했습니다.")
                        .build());
    }

    @PostMapping("/admin/update-answer")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> updateAnswer(
            @RequestBody @Valid final UpdateAnswerRequest updateAnswerRequest
    ) {
        answerService.updateAnswer(updateAnswerRequest);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("답변 수정에 성공했습니다.")
                        .build());
    }

    @PostMapping("/admin/delete-answer/{answerId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteAnswer(
            @PathVariable @Valid final Long answerId
    ) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("답변 삭제에 성공했습니다.")
                        .build());
    }
}
