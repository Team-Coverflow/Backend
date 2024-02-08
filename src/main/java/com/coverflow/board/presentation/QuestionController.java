package com.coverflow.board.presentation;

import com.coverflow.board.application.QuestionService;
import com.coverflow.board.dto.response.QuestionResponse;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/question")
@RestController
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/find-question")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<QuestionResponse>> findQuestionById(
            @RequestParam("id") @Valid long id
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<QuestionResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 질문 조회에 성공했습니다.")
                        .data(questionService.findQuestionById(id))
                        .build()
                );
    }
}
